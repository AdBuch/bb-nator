package ch.bbnator.identity.infrastructure;

import ch.bbnator.identity.application.BootstrapAdministrator;
import ch.bbnator.identity.application.ResolveSignInAccount;
import ch.bbnator.identity.application.UserAccountRepository;
import ch.bbnator.tenancy.infrastructure.TenantScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/** Wires sign-in per ADR-0002: LDAP verifies credentials, BB-NATOR owns accounts and roles. */
@Configuration(proxyBeanMethods = false)
public class IdentityModuleConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityModuleConfiguration.class);

    @Bean
    UserAccountRepository userAccountRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcUserAccountRepository(jdbcTemplate);
    }

    @Bean
    ResolveSignInAccount resolveSignInAccount(UserAccountRepository repository) {
        return new ResolveSignInAccount(repository);
    }

    @Bean
    BootstrapAdministrator bootstrapAdministrator(UserAccountRepository repository) {
        return new BootstrapAdministrator(repository);
    }

    @Bean
    AuthenticationManager authenticationManager(
            ResolveSignInAccount resolveSignInAccount,
            @Value("${bbnator.auth.tenant-id}") String tenantId,
            @Value("${bbnator.auth.ldap.url:}") String url,
            @Value("${bbnator.auth.ldap.base:}") String base,
            @Value("${bbnator.auth.ldap.user-search-base:ou=people}") String userSearchBase,
            @Value("${bbnator.auth.ldap.user-search-filter:(uid={0})}") String userSearchFilter,
            @Value("${bbnator.auth.ldap.subject-attribute:uid}") String subjectAttribute,
            @Value("${bbnator.auth.ldap.manager-dn:}") String managerDn,
            @Value("${bbnator.auth.ldap.manager-password:}") String managerPassword,
            @Value("${bbnator.auth.ldap.allow-insecure:false}") boolean allowInsecure) {
        TenantScope.validate(tenantId);
        if (url.isBlank()) {
            LOG.warn("LDAP_URL is not set: sign-in is disabled.");
            return new ProviderManager(signInDisabled());
        }
        if (!url.startsWith("ldaps://") && !allowInsecure) {
            throw new IllegalStateException("LDAP_URL must use ldaps://. Plain ldap:// is only allowed with LDAP_ALLOW_INSECURE=true for local tests.");
        }
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(List.of(url), base);
        if (!managerDn.isBlank()) {
            contextSource.setUserDn(managerDn);
            contextSource.setPassword(managerPassword);
        }
        contextSource.afterPropertiesSet();

        FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch(userSearchBase, userSearchFilter, contextSource);
        userSearch.setReturningAttributes(new String[]{"*", subjectAttribute});
        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
        bindAuthenticator.setUserSearch(userSearch);

        ProviderManager manager = new ProviderManager(
                new LdapLocalAccountAuthenticationProvider(bindAuthenticator, subjectAttribute, tenantId, resolveSignInAccount));
        manager.setEraseCredentialsAfterAuthentication(true);
        return manager;
    }

    @Bean
    ApplicationRunner bootstrapAdministratorRunner(
            BootstrapAdministrator bootstrapAdministrator,
            TransactionTemplate transactionTemplate,
            @Value("${bbnator.auth.tenant-id}") String tenantId,
            @Value("${bbnator.bootstrap.tenant-name:Lokaler Lehrbetrieb}") String tenantName,
            @Value("${bbnator.bootstrap.admin-subject:}") String adminSubject) {
        return args -> {
            Boolean created = transactionTemplate.execute(status -> bootstrapAdministrator.execute(tenantId, tenantName, adminSubject));
            if (Boolean.TRUE.equals(created)) {
                LOG.info("Created initial administrator for tenant {}.", tenantId);
            }
        };
    }

    private static AuthenticationProvider signInDisabled() {
        return new AuthenticationProvider() {
            @Override
            public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) {
                throw new InternalAuthenticationServiceException("Sign-in is not configured");
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }
}

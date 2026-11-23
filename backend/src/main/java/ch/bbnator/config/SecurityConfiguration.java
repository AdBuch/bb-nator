package ch.bbnator.config;

import ch.bbnator.identity.application.ResolveSignInAccount;
import ch.bbnator.identity.infrastructure.ActiveAccountFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@Profile("!demo")
public class SecurityConfiguration {
    /** Angular reads XSRF-TOKEN and sends it back as X-XSRF-TOKEN on state-changing requests. */
    @Bean
    CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieCustomizer(cookie -> cookie.path("/").sameSite("Lax"));
        return repository;
    }

    /**
     * Fail-closed policy: technical status, health and the sign-in endpoints are reachable;
     * everything else is denied until a feature explicitly opens it for a role.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CsrfTokenRepository csrfTokenRepository,
                                           ObjectProvider<ResolveSignInAccount> resolveSignInAccount) throws Exception {
        resolveSignInAccount.ifAvailable(resolver ->
                http.addFilterBefore(new ActiveAccountFilter(resolver), AnonymousAuthenticationFilter.class));
        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/system/status", "/actuator/health", "/api/v1/session/csrf").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/session/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/session/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/session/logout").authenticated()
                        .anyRequest().denyAll())
                .build();
    }
}

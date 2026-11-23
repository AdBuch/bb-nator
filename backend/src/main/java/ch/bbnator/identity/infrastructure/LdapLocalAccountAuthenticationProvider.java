package ch.bbnator.identity.infrastructure;

import ch.bbnator.identity.application.ResolveSignInAccount;
import ch.bbnator.identity.application.SignedInUser;
import ch.bbnator.identity.domain.UserAccount;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.authentication.LdapAuthenticator;

import java.util.List;

/**
 * Step 1: the directory verifies the password (LDAP bind).
 * Step 2: the local account must exist, be active and have a role.
 * Both failures look identical to the caller, so the endpoint does not reveal which accounts exist.
 */
public class LdapLocalAccountAuthenticationProvider implements AuthenticationProvider {
    static final String GENERIC_FAILURE = "Invalid credentials";
    private static final int MAX_USERNAME_LENGTH = 128;

    private final LdapAuthenticator directory;
    private final String subjectAttribute;
    private final String tenantId;
    private final ResolveSignInAccount resolveSignInAccount;

    public LdapLocalAccountAuthenticationProvider(LdapAuthenticator directory, String subjectAttribute,
                                                  String tenantId, ResolveSignInAccount resolveSignInAccount) {
        this.directory = directory;
        this.subjectAttribute = subjectAttribute;
        this.tenantId = tenantId;
        this.resolveSignInAccount = resolveSignInAccount;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        if (username == null || username.isBlank() || username.length() > MAX_USERNAME_LENGTH
                || !(authentication.getCredentials() instanceof String password) || password.isEmpty()) {
            throw new BadCredentialsException(GENERIC_FAILURE);
        }
        DirContextOperations entry;
        try {
            entry = directory.authenticate((UsernamePasswordAuthenticationToken) authentication);
        } catch (InternalAuthenticationServiceException unavailable) {
            throw unavailable;
        } catch (AuthenticationException rejected) {
            throw new BadCredentialsException(GENERIC_FAILURE);
        } catch (RuntimeException directoryError) {
            throw new InternalAuthenticationServiceException("Directory unavailable");
        }
        String subject = entry.getStringAttribute(subjectAttribute);
        UserAccount account = resolveSignInAccount.execute(tenantId, subject)
                .orElseThrow(() -> new BadCredentialsException(GENERIC_FAILURE));
        List<SimpleGrantedAuthority> authorities = account.roles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
        return UsernamePasswordAuthenticationToken.authenticated(SignedInUser.of(account), null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

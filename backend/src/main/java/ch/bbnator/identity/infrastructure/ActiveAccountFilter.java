package ch.bbnator.identity.infrastructure;

import ch.bbnator.identity.application.ResolveSignInAccount;
import ch.bbnator.identity.application.SignedInUser;
import ch.bbnator.identity.domain.UserAccount;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Re-checks the local account on every authenticated request.
 * Deactivation or removal of all roles ends the session immediately; changed roles apply on the next request.
 * Not a Spring bean on purpose, so it runs only inside the security filter chain.
 */
public class ActiveAccountFilter extends OncePerRequestFilter {
    private final ResolveSignInAccount resolveSignInAccount;
    private final HttpSessionSecurityContextRepository sessionRepository = new HttpSessionSecurityContextRepository();

    public ActiveAccountFilter(ResolveSignInAccount resolveSignInAccount) {
        this.resolveSignInAccount = resolveSignInAccount;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SignedInUser user) {
            Optional<UserAccount> current = resolveSignInAccount.execute(user.tenantId(), user.subject());
            if (current.isEmpty()) {
                SecurityContextHolder.clearContext();
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
            } else if (!current.get().roles().equals(user.roles())) {
                SecurityContext refreshed = SecurityContextHolder.createEmptyContext();
                refreshed.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(
                        SignedInUser.of(current.get()), null,
                        current.get().roles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).toList()));
                SecurityContextHolder.setContext(refreshed);
                sessionRepository.saveContext(refreshed, request, response);
            }
        }
        chain.doFilter(request, response);
    }
}

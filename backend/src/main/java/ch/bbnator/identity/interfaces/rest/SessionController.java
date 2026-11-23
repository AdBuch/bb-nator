package ch.bbnator.identity.interfaces.rest;

import ch.bbnator.identity.application.SignedInUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Session-based sign-in for the Angular client (ADR-0002).
 * Flow: GET /csrf, then POST /login with the X-XSRF-TOKEN header, then GET /me.
 */
@RestController
@RequestMapping("/api/v1/session")
public class SessionController {
    private final AuthenticationManager authenticationManager;
    private final ObjectProvider<CsrfTokenRepository> csrfTokenRepository;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public SessionController(AuthenticationManager authenticationManager, ObjectProvider<CsrfTokenRepository> csrfTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.csrfTokenRepository = csrfTokenRepository;
    }

    public record LoginRequest(@NotBlank @Size(max = 128) String username, @NotBlank @Size(max = 256) String password) {
    }

    public record SessionResponse(String subject, String displayName, String tenantId, List<String> roles) {
        static SessionResponse from(SignedInUser user) {
            return new SessionResponse(user.subject(), user.displayName(), user.tenantId(),
                    user.roles().stream().map(Enum::name).sorted().toList());
        }
    }

    /** Issues the CSRF cookie; the token is also returned for clients that cannot read cookies. */
    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("headerName", token.getHeaderName(), "token", token.getToken());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest body, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(body.username(), body.password()));
        } catch (InternalAuthenticationServiceException unavailable) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("error", "sign_in_unavailable"));
        } catch (AuthenticationException rejected) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "invalid_credentials"));
        }
        // New session id after login (session fixation), fresh CSRF token for the authenticated session.
        request.getSession(true);
        request.changeSessionId();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        csrfTokenRepository.ifAvailable(repository -> repository.saveToken(null, request, response));
        return ResponseEntity.ok(SessionResponse.from((SignedInUser) authentication.getPrincipal()));
    }

    @GetMapping("/me")
    public SessionResponse me(@AuthenticationPrincipal SignedInUser user) {
        return SessionResponse.from(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        csrfTokenRepository.ifAvailable(repository -> repository.saveToken(null, request, response));
        return ResponseEntity.noContent().build();
    }
}

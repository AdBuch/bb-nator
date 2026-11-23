package ch.bbnator.identity;

import ch.bbnator.identity.application.BootstrapAdministrator;
import ch.bbnator.identity.application.ResolveSignInAccount;
import ch.bbnator.identity.application.UserAccountRepository;
import ch.bbnator.identity.domain.Role;
import ch.bbnator.identity.domain.UserAccount;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pure use-case tests with an in-memory repository; no Spring. */
class ResolveSignInAccountTest {
    private final Map<String, UserAccount> accounts = new HashMap<>();
    private final UserAccountRepository repository = new UserAccountRepository() {
        @Override
        public Optional<UserAccount> findBySubject(String tenantId, String subject) {
            return Optional.ofNullable(accounts.get(tenantId + "/" + subject));
        }

        @Override
        public void createTenantIfMissing(String tenantId, String tenantName) {
        }

        @Override
        public void createUser(String tenantId, String subject, String displayName, Set<Role> roles) {
            accounts.put(tenantId + "/" + subject, new UserAccount(UUID.randomUUID(), tenantId, subject, displayName, true, roles));
        }
    };

    private void put(String subject, boolean active, Set<Role> roles) {
        accounts.put("t1/" + subject, new UserAccount(UUID.randomUUID(), "t1", subject, subject, active, roles));
    }

    @Test
    void activeUserWithRoleMaySignIn() {
        put("a", true, Set.of(Role.MENTOR));
        assertTrue(new ResolveSignInAccount(repository).execute("t1", "a").isPresent());
    }

    @Test
    void inactiveOrRolelessOrBlankSubjectMayNot() {
        put("inactive", false, Set.of(Role.MENTOR));
        put("noroles", true, Set.of());
        ResolveSignInAccount useCase = new ResolveSignInAccount(repository);
        assertFalse(useCase.execute("t1", "inactive").isPresent());
        assertFalse(useCase.execute("t1", "noroles").isPresent());
        assertFalse(useCase.execute("t1", " ").isPresent());
        assertFalse(useCase.execute("t1", null).isPresent());
        assertFalse(useCase.execute("t2", "inactive").isPresent());
    }

    @Test
    void bootstrapCreatesAdministratorOnce() {
        BootstrapAdministrator bootstrap = new BootstrapAdministrator(repository);
        assertTrue(bootstrap.execute("t1", "Fiktiv", "root"));
        assertFalse(bootstrap.execute("t1", "Fiktiv", "root"));
        assertFalse(bootstrap.execute("t1", "Fiktiv", ""));
        assertEquals(Set.of(Role.ADMIN), accounts.get("t1/root").roles());
    }
}

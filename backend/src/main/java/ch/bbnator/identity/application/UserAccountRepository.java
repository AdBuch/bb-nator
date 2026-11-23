package ch.bbnator.identity.application;

import ch.bbnator.identity.domain.Role;
import ch.bbnator.identity.domain.UserAccount;

import java.util.Optional;
import java.util.Set;

/** Port for local user accounts; every call is scoped to one tenant. */
public interface UserAccountRepository {
    Optional<UserAccount> findBySubject(String tenantId, String subject);

    void createTenantIfMissing(String tenantId, String tenantName);

    void createUser(String tenantId, String subject, String displayName, Set<Role> roles);
}

package ch.bbnator.identity.application;

import ch.bbnator.identity.domain.Role;

import java.util.Set;

/** Use case: create the tenant and the first administrator once, so a fresh installation is usable. */
public final class BootstrapAdministrator {
    private final UserAccountRepository repository;

    public BootstrapAdministrator(UserAccountRepository repository) {
        this.repository = repository;
    }

    /** @return true if a new administrator was created */
    public boolean execute(String tenantId, String tenantName, String subject) {
        if (subject == null || subject.isBlank()) {
            return false;
        }
        repository.createTenantIfMissing(tenantId, tenantName);
        if (repository.findBySubject(tenantId, subject).isPresent()) {
            return false;
        }
        repository.createUser(tenantId, subject, subject, Set.of(Role.ADMIN));
        return true;
    }
}

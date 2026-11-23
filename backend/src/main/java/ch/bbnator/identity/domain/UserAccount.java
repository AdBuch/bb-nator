package ch.bbnator.identity.domain;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** A locally registered user. A successful directory bind alone grants nothing without this record. */
public record UserAccount(UUID id, String tenantId, String subject, String displayName, boolean active, Set<Role> roles) {
    public UserAccount {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(tenantId, "tenantId");
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(displayName, "displayName");
        roles = Set.copyOf(roles);
    }

    /** Only active users with at least one role may sign in. */
    public boolean maySignIn() {
        return active && !roles.isEmpty();
    }
}

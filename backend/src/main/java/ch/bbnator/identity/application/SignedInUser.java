package ch.bbnator.identity.application;

import ch.bbnator.identity.domain.Role;
import ch.bbnator.identity.domain.UserAccount;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/** Session principal. Holds no credentials and no directory attributes beyond the subject. */
public record SignedInUser(UUID userId, String tenantId, String subject, String displayName, Set<Role> roles)
        implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public SignedInUser {
        roles = Set.copyOf(roles);
    }

    public static SignedInUser of(UserAccount account) {
        return new SignedInUser(account.id(), account.tenantId(), account.subject(), account.displayName(), account.roles());
    }
}

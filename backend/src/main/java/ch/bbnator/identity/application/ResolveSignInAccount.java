package ch.bbnator.identity.application;

import ch.bbnator.identity.domain.UserAccount;

import java.util.Optional;

/** Use case: after the directory verified the password, find the local account allowed to sign in. */
public final class ResolveSignInAccount {
    private final UserAccountRepository repository;

    public ResolveSignInAccount(UserAccountRepository repository) {
        this.repository = repository;
    }

    public Optional<UserAccount> execute(String tenantId, String subject) {
        if (subject == null || subject.isBlank()) {
            return Optional.empty();
        }
        return repository.findBySubject(tenantId, subject).filter(UserAccount::maySignIn);
    }
}

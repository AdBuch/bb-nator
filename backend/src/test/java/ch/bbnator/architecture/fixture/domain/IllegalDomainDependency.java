package ch.bbnator.architecture.fixture.domain;

import org.springframework.context.ApplicationContext;

/** Intentionally illegal dependency used only to prove the architecture rule rejects it. */
public final class IllegalDomainDependency {
    private ApplicationContext context;
}

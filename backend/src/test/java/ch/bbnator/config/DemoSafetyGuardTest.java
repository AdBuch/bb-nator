package ch.bbnator.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DemoSafetyGuardTest {
    @Test
    void refusesWithoutAcknowledgement() {
        assertThrows(IllegalStateException.class, () -> DemoSafetyGuard.check(new MockEnvironment()));
    }

    @Test
    void refusesTogetherWithProdProfile() {
        MockEnvironment env = new MockEnvironment().withProperty("bbnator.demo.acknowledged", "true");
        env.setActiveProfiles("demo", "prod");
        assertThrows(IllegalStateException.class, () -> DemoSafetyGuard.check(env));
    }

    @Test
    void refusesWhenDirectorySignInIsConfigured() {
        MockEnvironment env = new MockEnvironment()
                .withProperty("bbnator.demo.acknowledged", "true")
                .withProperty("bbnator.auth.ldap.url", "ldaps://directory.example.org");
        assertThrows(IllegalStateException.class, () -> DemoSafetyGuard.check(env));
    }

    @Test
    void startsWhenAcknowledgedLocally() {
        MockEnvironment env = new MockEnvironment().withProperty("bbnator.demo.acknowledged", "true");
        env.setActiveProfiles("demo");
        assertDoesNotThrow(() -> DemoSafetyGuard.check(env));
    }
}

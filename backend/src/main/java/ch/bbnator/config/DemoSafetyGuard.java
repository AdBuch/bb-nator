package ch.bbnator.config;

import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Stops the unauthenticated demo profile from starting in a configuration that looks like real use.
 * This guards against misconfiguration; network exposure is still controlled by compose.yaml (127.0.0.1 only).
 */
public final class DemoSafetyGuard {
    private DemoSafetyGuard() {
    }

    public static void check(Environment environment) {
        if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
            throw new IllegalStateException("The demo profile must not be combined with the prod profile.");
        }
        String ldapUrl = environment.getProperty("bbnator.auth.ldap.url", "");
        if (!ldapUrl.isBlank()) {
            throw new IllegalStateException("The demo profile must not run with directory sign-in configured (LDAP_URL).");
        }
        if (!environment.getProperty("bbnator.demo.acknowledged", Boolean.class, false)) {
            throw new IllegalStateException(
                    "The demo profile exposes unauthenticated write endpoints with fictional data. "
                            + "Set BBNATOR_DEMO_ACKNOWLEDGED=true only on a local, non-shared machine.");
        }
    }
}

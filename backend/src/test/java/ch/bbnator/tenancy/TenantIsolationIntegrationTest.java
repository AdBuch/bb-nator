package ch.bbnator.tenancy;

import ch.bbnator.integration.PostgresTestConfiguration;
import ch.bbnator.tenancy.infrastructure.TenantScope;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Proves row-level security on real PostgreSQL. Every test runs in one transaction
 * (rolled back afterwards), so seed and assertions share the same connection.
 */
@SpringBootTest
@Import(PostgresTestConfiguration.class)
@Transactional
class TenantIsolationIntegrationTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void tenantSeesOnlyOwnUsers() {
        seedTenants();
        TenantScope.bind(entityManager, "tenant-a");
        assertEquals(1, countUsers());
        assertEquals("tenant-a", jdbcTemplate.queryForObject("SELECT tenant_id FROM app_user", String.class));
    }

    @Test
    void withoutTenantContextNothingIsVisible() {
        seedTenants();
        assertEquals(0, countUsers());
    }

    @Test
    void tenantCannotInsertIntoForeignTenant() {
        seedTenants();
        TenantScope.bind(entityManager, "tenant-a");
        assertThrows(Exception.class, () -> jdbcTemplate.update(
                "INSERT INTO app_user (id, tenant_id, ldap_subject, display_name) VALUES (?,?,?,?)",
                UUID.randomUUID(), "tenant-b", "mallory", "Fiktiv Mallory"));
    }

    @Test
    void invalidTenantIdentifierIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> TenantScope.bind(entityManager, "x'; DROP TABLE app_user;--"));
    }

    /** Seeds two fictional tenants; each user is inserted under its own tenant context. */
    private void seedTenants() {
        jdbcTemplate.update("INSERT INTO tenant (id, name) VALUES ('tenant-a','Fiktiv A'),('tenant-b','Fiktiv B')");
        TenantScope.bind(entityManager, "tenant-a");
        insertUser("tenant-a", "alice-a");
        TenantScope.bind(entityManager, "tenant-b");
        insertUser("tenant-b", "bob-b");
        // Reset to no tenant so the seeding context does not leak into the test body.
        jdbcTemplate.queryForObject("SELECT set_config('app.tenant_id', '', true)", String.class);
    }

    private void insertUser(String tenant, String subject) {
        jdbcTemplate.update("INSERT INTO app_user (id, tenant_id, ldap_subject, display_name) VALUES (?,?,?,?)",
                UUID.randomUUID(), tenant, subject, "Fiktiv " + subject);
    }

    private int countUsers() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM app_user", Integer.class);
    }
}

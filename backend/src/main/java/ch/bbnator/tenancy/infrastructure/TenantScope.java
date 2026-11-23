package ch.bbnator.tenancy.infrastructure;

import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Binds the current tenant to the running PostgreSQL transaction.
 * Must be called first in every transaction that touches tenant-scoped tables;
 * the value is transaction-local ({@code set_config(..., true)}) and never leaks to the pool.
 */
public final class TenantScope {
    private static final Pattern TENANT_ID = Pattern.compile("^[a-z0-9][a-z0-9-]{1,62}$");

    private TenantScope() {
    }

    public static void bind(EntityManager entityManager, String tenantId) {
        validate(tenantId);
        entityManager.createNativeQuery("SELECT set_config('app.tenant_id', :tenant, true)")
                .setParameter("tenant", tenantId)
                .getSingleResult();
    }

    /** Same as {@link #bind(EntityManager, String)} for JDBC access inside a Spring-managed transaction. */
    public static void bind(JdbcTemplate jdbcTemplate, String tenantId) {
        validate(tenantId);
        jdbcTemplate.queryForObject("SELECT set_config('app.tenant_id', ?, true)", String.class, tenantId);
    }

    public static void validate(String tenantId) {
        Objects.requireNonNull(tenantId, "tenantId");
        if (!TENANT_ID.matcher(tenantId).matches()) {
            throw new IllegalArgumentException("Invalid tenant identifier");
        }
    }
}

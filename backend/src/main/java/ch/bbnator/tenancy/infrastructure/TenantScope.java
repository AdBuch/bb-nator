package ch.bbnator.tenancy.infrastructure;

import jakarta.persistence.EntityManager;
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
        Objects.requireNonNull(tenantId, "tenantId");
        if (!TENANT_ID.matcher(tenantId).matches()) {
            throw new IllegalArgumentException("Invalid tenant identifier");
        }
        entityManager.createNativeQuery("SELECT set_config('app.tenant_id', :tenant, true)")
                .setParameter("tenant", tenantId)
                .getSingleResult();
    }
}

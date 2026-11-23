package ch.bbnator.identity.infrastructure;

import ch.bbnator.identity.application.UserAccountRepository;
import ch.bbnator.identity.domain.Role;
import ch.bbnator.identity.domain.UserAccount;
import ch.bbnator.tenancy.infrastructure.TenantScope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * JDBC adapter. Binds the tenant for row-level security AND filters by tenant explicitly,
 * so isolation holds even where RLS is bypassed (for example a superuser in local development).
 */
public class JdbcUserAccountRepository implements UserAccountRepository {
    private final JdbcTemplate jdbc;

    public JdbcUserAccountRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAccount> findBySubject(String tenantId, String subject) {
        TenantScope.bind(jdbc, tenantId);
        List<Object[]> rows = jdbc.query(
                "SELECT id, display_name, active FROM app_user WHERE tenant_id = ? AND ldap_subject = ?",
                (rs, i) -> new Object[]{rs.getObject("id", UUID.class), rs.getString("display_name"), rs.getBoolean("active")},
                tenantId, subject);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        UUID id = (UUID) rows.getFirst()[0];
        Set<Role> roles = EnumSet.noneOf(Role.class);
        jdbc.queryForList("SELECT role FROM role_assignment WHERE tenant_id = ? AND user_id = ?", String.class, tenantId, id)
                .forEach(role -> roles.add(Role.valueOf(role)));
        return Optional.of(new UserAccount(id, tenantId, subject, (String) rows.getFirst()[1], (Boolean) rows.getFirst()[2], roles));
    }

    @Override
    @Transactional
    public void createTenantIfMissing(String tenantId, String tenantName) {
        TenantScope.validate(tenantId);
        jdbc.update("INSERT INTO tenant (id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING", tenantId, tenantName);
    }

    @Override
    @Transactional
    public void createUser(String tenantId, String subject, String displayName, Set<Role> roles) {
        TenantScope.bind(jdbc, tenantId);
        UUID userId = UUID.randomUUID();
        jdbc.update("INSERT INTO app_user (id, tenant_id, ldap_subject, display_name) VALUES (?, ?, ?, ?)",
                userId, tenantId, subject, displayName);
        for (Role role : roles) {
            jdbc.update("INSERT INTO role_assignment (id, tenant_id, user_id, role) VALUES (?, ?, ?, ?)",
                    UUID.randomUUID(), tenantId, userId, role.name());
        }
    }
}

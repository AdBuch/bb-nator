package ch.bbnator.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;

/** The afterMigrate callback must enable login for bbnator_app with exactly APP_DB_PASSWORD. */
@SpringBootTest(properties = "spring.flyway.placeholders.appdbpassword=test-only-app-pw-42")
@Import(PostgresTestConfiguration.class)
class RuntimeRoleIntegrationTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void applicationRoleLogsInWithConfiguredPasswordAndIsRlsBound() throws Exception {
        String url;
        try (Connection owner = dataSource.getConnection()) {
            url = owner.getMetaData().getURL();
        }
        try (Connection app = DriverManager.getConnection(url, "bbnator_app", "test-only-app-pw-42");
             ResultSet rs = app.createStatement().executeQuery(
                     "SELECT current_user, rolsuper, rolbypassrls FROM pg_roles WHERE rolname = current_user")) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getString(1)).isEqualTo("bbnator_app");
            assertThat(rs.getBoolean(2)).isFalse();
            assertThat(rs.getBoolean(3)).isFalse();
        }
    }

    @Test
    void applicationRoleCannotTouchMigrationHistory() {
        Boolean canRead = jdbc.queryForObject(
                "SELECT has_table_privilege('bbnator_app', 'flyway_schema_history', 'SELECT')", Boolean.class);
        assertThat(canRead).isFalse();
    }
}

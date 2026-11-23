package ch.bbnator.identity;

import ch.bbnator.integration.PostgresTestConfiguration;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end sign-in against an in-memory LDAP directory and real PostgreSQL.
 * All people are fictional.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgresTestConfiguration.class)
class LdapSignInIntegrationTest {
    private static final String TENANT = "test-local";
    private static final InMemoryDirectoryServer DIRECTORY = startDirectory();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JdbcTemplate jdbc;

    private static InMemoryDirectoryServer startDirectory() {
        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=example,dc=org");
            config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("default", 0));
            config.setSchema(null);
            InMemoryDirectoryServer server = new InMemoryDirectoryServer(config);
            server.add("dn: dc=example,dc=org", "objectClass: top", "objectClass: domain", "dc: example");
            server.add("dn: ou=people,dc=example,dc=org", "objectClass: organizationalUnit", "ou: people");
            for (String uid : new String[]{"mia.admin", "noah.unknown", "lea.inactive", "ben.foreign", "tim.noroles"}) {
                server.add("dn: uid=" + uid + ",ou=people,dc=example,dc=org", "objectClass: inetOrgPerson",
                        "uid: " + uid, "cn: Fiktiv " + uid, "sn: Fiktiv", "userPassword: pw-" + uid);
            }
            server.startListening();
            return server;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @DynamicPropertySource
    static void ldap(DynamicPropertyRegistry registry) {
        registry.add("bbnator.auth.tenant-id", () -> TENANT);
        registry.add("bbnator.auth.ldap.url", () -> "ldap://localhost:" + DIRECTORY.getListenPort());
        registry.add("bbnator.auth.ldap.base", () -> "dc=example,dc=org");
        registry.add("bbnator.auth.ldap.allow-insecure", () -> "true");
        registry.add("bbnator.bootstrap.admin-subject", () -> "mia.admin");
    }

    @AfterAll
    static void stopDirectory() {
        DIRECTORY.shutDown(true);
    }

    @BeforeEach
    void seedLocalAccounts() {
        jdbc.update("INSERT INTO tenant (id, name) VALUES ('other-tenant', 'Fiktiv Andere') ON CONFLICT DO NOTHING");
        insertUser(TENANT, "lea.inactive", false, "APPRENTICE");
        insertUser("other-tenant", "ben.foreign", true, "MENTOR");
        insertUser(TENANT, "tim.noroles", true, null);
    }

    private void insertUser(String tenant, String subject, boolean active, String role) {
        if (jdbc.queryForObject("SELECT count(*) FROM app_user WHERE tenant_id = ? AND ldap_subject = ?", Integer.class, tenant, subject) > 0) {
            return;
        }
        UUID id = UUID.randomUUID();
        jdbc.update("INSERT INTO app_user (id, tenant_id, ldap_subject, display_name, active) VALUES (?,?,?,?,?)",
                id, tenant, subject, "Fiktiv " + subject, active);
        if (role != null) {
            jdbc.update("INSERT INTO role_assignment (id, tenant_id, user_id, role) VALUES (?,?,?,?)", UUID.randomUUID(), tenant, id, role);
        }
    }

    private Cookie csrfCookie(MockHttpSession session) throws Exception {
        var request = get("/api/v1/session/csrf");
        if (session != null) {
            request.session(session);
        }
        Cookie cookie = mvc.perform(request).andExpect(status().isOk()).andReturn().getResponse().getCookie("XSRF-TOKEN");
        assertThat(cookie).isNotNull();
        return cookie;
    }

    private MvcResult login(String username, String password) throws Exception {
        Cookie csrf = csrfCookie(null);
        return mvc.perform(post("/api/v1/session/login")
                        .cookie(csrf).header("X-XSRF-TOKEN", csrf.getValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andReturn();
    }

    @Test
    void bootstrapAdministratorSignsInAndSeesOwnSession() throws Exception {
        MvcResult result = login("mia.admin", "pw-mia.admin");
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertThat(session).isNotNull();

        mvc.perform(get("/api/v1/session/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("mia.admin"))
                .andExpect(jsonPath("$.tenantId").value(TENANT))
                .andExpect(jsonPath("$.roles[0]").value("ADMIN"));
    }

    @Test
    void wrongPasswordIsRejected() throws Exception {
        assertThat(login("mia.admin", "wrong").getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    void directoryAccountWithoutLocalUserIsRejected() throws Exception {
        assertThat(login("noah.unknown", "pw-noah.unknown").getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    void inactiveLocalUserIsRejected() throws Exception {
        assertThat(login("lea.inactive", "pw-lea.inactive").getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    void localUserWithoutRoleIsRejected() throws Exception {
        assertThat(login("tim.noroles", "pw-tim.noroles").getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    void userOfAnotherTenantIsRejected() throws Exception {
        assertThat(login("ben.foreign", "pw-ben.foreign").getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    void rejectionsLookIdentical() throws Exception {
        String wrongPassword = login("mia.admin", "wrong").getResponse().getContentAsString();
        String unknownUser = login("noah.unknown", "pw-noah.unknown").getResponse().getContentAsString();
        assertThat(unknownUser).isEqualTo(wrongPassword);
    }

    @Test
    void loginWithoutCsrfTokenIsForbidden() throws Exception {
        mvc.perform(post("/api/v1/session/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"mia.admin\",\"password\":\"pw-mia.admin\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void sessionEndpointRequiresSignIn() throws Exception {
        mvc.perform(get("/api/v1/session/me")).andExpect(status().isForbidden());
    }

    @Test
    void signedInUserStillCannotReachUnopenedRoutes() throws Exception {
        MockHttpSession session = (MockHttpSession) login("mia.admin", "pw-mia.admin").getRequest().getSession(false);
        mvc.perform(get("/api/v1/apprentices").session(session)).andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/demo/learners").session(session)).andExpect(status().isForbidden());
    }

    @Test
    void logoutEndsTheSession() throws Exception {
        MockHttpSession session = (MockHttpSession) login("mia.admin", "pw-mia.admin").getRequest().getSession(false);
        Cookie csrf = csrfCookie(session);
        mvc.perform(post("/api/v1/session/logout").session(session).cookie(csrf).header("X-XSRF-TOKEN", csrf.getValue()))
                .andExpect(status().isNoContent());
        assertThat(session.isInvalid()).isTrue();
    }

    @Test
    void bootstrapIsIdempotent() {
        Integer admins = jdbc.queryForObject(
                "SELECT count(*) FROM app_user WHERE tenant_id = ? AND ldap_subject = 'mia.admin'", Integer.class, TENANT);
        assertThat(admins).isEqualTo(1);
    }
}

package ch.bbnator.system.interfaces.rest;

import ch.bbnator.config.SecurityConfiguration;
import ch.bbnator.system.infrastructure.SystemModuleConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Proves the fail-closed foundation policy without a database. */
@WebMvcTest(SystemController.class)
@Import({SecurityConfiguration.class, SystemModuleConfiguration.class})
class SystemControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void statusIsPublicAndStructured() throws Exception {
        mockMvc.perform(get("/api/v1/system/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("bb-nator"))
                .andExpect(jsonPath("$.status").value("READY"))
                .andExpect(jsonPath("$.version").value("0.1.0"));
    }

    @Test
    void unknownApiRoutesAreDenied() throws Exception {
        mockMvc.perform(get("/api/v1/apprentices")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/learning-tasks/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/")).andExpect(status().isForbidden());
    }

    @Test
    void everyWriteMethodIsDeniedOnPublicAndBusinessPaths() throws Exception {
        for (String path : new String[] {
                "/api/v1/system/status",
                "/actuator/health",
                "/api/v1/apprentices",
                "/api/v1/learning-tasks/1"
        }) {
            mockMvc.perform(post(path)).andExpect(status().isForbidden());
            mockMvc.perform(put(path)).andExpect(status().isForbidden());
            mockMvc.perform(patch(path)).andExpect(status().isForbidden());
            mockMvc.perform(delete(path)).andExpect(status().isForbidden());
        }
    }

    @Test
    void dotSegmentsAndEncodedTraversalCannotBecomePublicRoutes() throws Exception {
        for (String path : new String[] {
                "/api/v1/system/status/../x",
                "/api/v1/system/%2e%2e/status",
                "/actuator/health/../env"
        }) {
            try {
                mockMvc.perform(get(path)).andExpect(status().is4xxClientError());
            } catch (RequestRejectedException rejected) {
                // StrictHttpFirewall may reject ambiguous paths before MVC handles them.
            }
        }
    }
}

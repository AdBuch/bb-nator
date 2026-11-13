package ch.bbnator.system.interfaces.rest;

import ch.bbnator.config.SecurityConfiguration;
import ch.bbnator.system.infrastructure.SystemModuleConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void writeMethodsOnPublicRouteAreDenied() throws Exception {
        mockMvc.perform(post("/api/v1/system/status")).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/v1/system/status")).andExpect(status().isForbidden());
    }
}

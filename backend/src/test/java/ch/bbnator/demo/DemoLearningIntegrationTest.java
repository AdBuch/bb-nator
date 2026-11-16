package ch.bbnator.demo;

import ch.bbnator.integration.PostgresTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("demo")
@Import(PostgresTestConfiguration.class)
class DemoLearningIntegrationTest {
    @Autowired MockMvc mvc;

    @Test void seededLearnersAndTasksAreVisibleOnlyInDemoMode() throws Exception {
        mvc.perform(get("/api/v1/demo/learners"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(4));
        mvc.perform(get("/api/v1/demo/tasks"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get("/api/v1/apprentices")).andExpect(status().isForbidden());
    }

    @Test void creatingAndReviewingDemoTaskPersists() throws Exception {
        var result = mvc.perform(post("/api/v1/demo/tasks")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content("{\"learnerId\":\"00000000-0000-4000-8000-000000000001\",\"title\":\"JUnit Test\",\"learningGoal\":\"Testing\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("ASSIGNED"))
            .andReturn();
        String data = result.getResponse().getContentAsString();
        String id = com.jayway.jsonpath.JsonPath.read(data, "$.id");
        mvc.perform(patch("/api/v1/demo/tasks/" + id)
                 .contentType(MediaType.APPLICATION_JSON)
                 .content("{\"status\":\"DONE\",\"evidence\":\"VERIFIED\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.evidence").value("VERIFIED"));
    }

    @Test void invalidInputIsRejected() throws Exception {
        mvc.perform(post("/api/v1/demo/tasks").contentType(MediaType.APPLICATION_JSON)
            .content("{\"learnerId\":\"00000000-0000-4000-8000-000000000000\",\"title\":\"x\",\"learningGoal\":\"x\"}"))
            .andExpect(status().isBadRequest());
    }
}

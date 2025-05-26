package de.twaslowski.moodtracker.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.IntegrationTestBase;
import de.twaslowski.moodtracker.entity.UserSpec;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@SpringBootTest
@AutoConfigureMockMvc
public class RecordControllerIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldReturnUnauthorizedIfUserNotAuthenticated() {
    mockMvc.perform(get("/api/v1/records"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldReturnOkIfUserAuthenticated() {
    var user = initializeUser(UserSpec.valid().build());
    mockMvc.perform(get("/api/v1/records")
            .with(user(user)))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldReturnSingleRecord() {
    var user = initializeUser(UserSpec.valid().build());
    recordService.recordFromBaseline(user);

    mockMvc.perform(get("/api/v1/records/")
            .with(user(user)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].datapoints").isArray())
        .andExpect(jsonPath("$[0].datapoints").isNotEmpty())
        .andReturn();
  }
}

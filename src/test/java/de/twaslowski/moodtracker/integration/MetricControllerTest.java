package de.twaslowski.moodtracker.integration;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.IntegrationTestBase;
import de.twaslowski.moodtracker.entity.UserSpec;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@IntegrationTest
@AutoConfigureMockMvc
public class MetricControllerTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldTrackExistingMetric() {
    var user = initializeUser(UserSpec.valid().build());
    assertThat(metricRepository.findById(1L)).isPresent();
    metricConfigurationRepository.deleteAll();
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/metric/tracking/1")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldReturnConflictOnMetricAlreadyTracked() {
    var user = initializeUser(UserSpec.valid().build());
    assertThat(metricRepository.findById(1L)).isPresent();
    metricConfigurationRepository.deleteAll();
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/metric/tracking/1")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andReturn();
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/metric/tracking/1")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(409))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldTrackAndUntrackMetric() {
    var user = initializeUser(UserSpec.valid().build());
    assertThat(metricRepository.findById(1L)).isPresent();
    metricConfigurationRepository.deleteAll();
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/metric/tracking/1")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andReturn();

    String trackedMetricId = JsonPath.read(result.getResponse().getContentAsString(), "$.trackedMetricId");

    mockMvc.perform(MockMvcRequestBuilders.delete(format("/api/v1/metric/tracking/%s", trackedMetricId))
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(204))
        .andReturn();
  }


  @Test
  @SneakyThrows
  void shouldReturnBadRequestOnMetricNotFound() {
    long nonexistentMetricId = 123L;
    var user = initializeUser(UserSpec.valid().build());
    assertThat(metricRepository.findById(nonexistentMetricId)).isEmpty();
    metricConfigurationRepository.deleteAll();
    mockMvc.perform(MockMvcRequestBuilders.post(format("/api/v1/metric/tracking/%d", nonexistentMetricId))
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldReturnUnprocessableEntityOnMetricNotTracked() {
    var user = initializeUser(UserSpec.valid().build());
    metricConfigurationRepository.deleteAll();
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/metric/tracking/abc-def")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(422))
        .andReturn();
  }
}

package de.twaslowski.moodtracker.integration;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.IntegrationTestBase;
import de.twaslowski.moodtracker.domain.dto.MetricDTO;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.entity.UserSpec;
import lombok.SneakyThrows;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static java.lang.String.format;
import java.util.List;

@SpringBootTest
@IntegrationTest
@AutoConfigureMockMvc
public class MetricControllerIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldReturnUserMetrics() {
    var user = initializeUser(UserSpec.valid().build());

    var metric = Metric.builder()
        .ownerId(user.getId())
        .name("metric")
        .description("metric")
        .minValue(-1)
        .defaultValue(1)
        .maxValue(1)
        .defaultMetric(false)
        .sortOrder(Metric.SortOrder.ASC)
        .build();
    metricRepository.save(metric);

    var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/metric")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    List<MetricDTO> data = objectMapper.readValue(result.getResponse().getContentAsString(),
        objectMapper.getTypeFactory().constructCollectionType(List.class, MetricDTO.class));

    // There should be exactly 3 metrics: 2 default metrics and the one we just created.
    assertThat(data).hasSize(3);
    assertThat(data.stream().filter(MetricDTO::isDefault)).hasSize(2);
  }

  @Test
  @SneakyThrows
  void shouldTrackExistingMetric() {
    var user = initializeUser(UserSpec.valid().build());
    var mood = metricRepository.findMetricsByDefaultMetricIsTrueAndNameEquals("Mood").orElseThrow();

    metricConfigurationRepository.deleteAll();
    assertThat(metricConfigurationRepository.findAll()).isEmpty();

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/metric/tracking/%d".formatted(mood.getId()))
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldReturnConflictOnMetricAlreadyTracked() {
    var user = initializeUser(UserSpec.valid().build());
    var mood = metricRepository.findMetricsByDefaultMetricIsTrueAndNameEquals("Mood").orElseThrow();
    metricConfigurationRepository.deleteAll();

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/metric/tracking/%d".formatted(mood.getId()))
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andReturn();
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/metric/tracking/%d".formatted(mood.getId()))
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(409))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldTrackAndUntrackMetric() {
    var user = initializeUser(UserSpec.valid().build());
    var mood = metricRepository.findMetricsByDefaultMetricIsTrueAndNameEquals("Mood").orElseThrow();

    metricConfigurationRepository.deleteAll();
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/metric/tracking/%d".formatted(mood.getId()))
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andReturn();

    mockMvc.perform(MockMvcRequestBuilders.delete(format("/api/v1/metric/tracking/%d", mood.getId()))
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(204))
        .andReturn();
  }


  @Test
  @SneakyThrows
  void shouldReturnNotFound() {
    long nonexistentMetricId = 0L;
    var user = initializeUser(UserSpec.valid().build());

    assertThat(metricRepository.findById(nonexistentMetricId)).isEmpty();

    metricConfigurationRepository.deleteAll();
    mockMvc.perform(MockMvcRequestBuilders.post(format("/api/v1/metric/tracking/%d", nonexistentMetricId))
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(404))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldReturnUnprocessableEntityOnMetricNotTracked() {
    var user = initializeUser(UserSpec.valid().build());
    metricConfigurationRepository.deleteAll();

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/metric/tracking/1")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andReturn();
  }
}

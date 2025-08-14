package de.twaslowski.moodtracker.integration;


import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.IntegrationTestBase;
import de.twaslowski.moodtracker.adapter.rest.v2.dto.MetricSeries;
import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
import de.twaslowski.moodtracker.entity.RecordSpec;
import de.twaslowski.moodtracker.entity.UserSpec;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@SpringBootTest
@AutoConfigureMockMvc
public class RecordControllerV2IntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldReturnTimeSeries() {
    var user = initializeUser(UserSpec.valid().build());

    var metrics = metricConfigurationRepository.findByUserId(user.getId());
    assertThat(metrics).hasSize(2);

    var metricsIds = metrics.stream()
        .map(metricConfiguration ->  metricConfiguration.getMetric().getId())
        .toList();

    var record = RecordSpec.forUser(user.getId(), metricsIds).build();
    var anotherRecord = RecordSpec.forUser(user.getId(), metricsIds)
        .creationTimestamp(now().minusDays(1))
        .build();

    recordRepository.saveAll(List.of(record, anotherRecord));

    var result = mockMvc.perform(get("/api/v2/records")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    List<MetricSeries> timeSeries = objectMapper.readValue(result.getResponse().getContentAsString(),
        objectMapper.getTypeFactory().constructCollectionType(List.class, MetricSeries.class));

    assertThat(timeSeries.size()).isEqualTo(2);

    var moodSeries = timeSeries.stream()
        .filter(series -> "Mood".equals(series.name()))
        .findFirst().orElseThrow();

    var sleepSeries = timeSeries.stream()
        .filter(series -> "Sleep".equals(series.name()))
        .findFirst().orElseThrow();

    assertThat(moodSeries.trackingData()).hasSize(2);
    assertThat(sleepSeries.trackingData()).hasSize(2);
  }
}

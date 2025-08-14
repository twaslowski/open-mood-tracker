package de.twaslowski.moodtracker.integration;


import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.IntegrationTestBase;
import de.twaslowski.moodtracker.adapter.rest.v2.dto.MetricSeries;
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

    assertThat(metricConfigurationRepository.findByUserId(user.getId())).hasSize(2);

    var record = RecordSpec.forUser(user.getId()).build();
    var anotherRecord = RecordSpec.forUser(user.getId())
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
    var moodSeries = timeSeries.stream().filter(series -> series.metricId() == 1).findFirst().orElseThrow();
    var sleepSeries = timeSeries.stream().filter(series -> series.metricId() == 2).findFirst().orElseThrow();

    assertThat(moodSeries.trackingData()).hasSize(2);
    assertThat(sleepSeries.trackingData()).hasSize(2);
  }
}

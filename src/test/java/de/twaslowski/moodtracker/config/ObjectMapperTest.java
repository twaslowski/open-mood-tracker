package de.twaslowski.moodtracker.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class ObjectMapperTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @SneakyThrows
  void shouldSerializeEmptyMetric() {
    var mood = new MetricDatapoint(1, null);
    var json = "{\"metricId\":1,\"value\":null}";

    assertThat(objectMapper.writeValueAsString(mood)).isEqualTo(json);
  }

  @Test
  @SneakyThrows
  void shouldSerializePopulatedMetric() {
    var mood = new MetricDatapoint(2, 8);
    var json = "{\"metricId\":2,\"value\":8}";

    assertThat(objectMapper.writeValueAsString(mood)).isEqualTo(json);
  }

  @Test
  @SneakyThrows
  void shouldSerializeListOfMetrics() {
    var metrics = List.of(
        new MetricDatapoint(1, 3),
        new MetricDatapoint(2, 8)
    );

    var serialized = objectMapper.writeValueAsString(metrics);
    assertThat(serialized).contains("\"metricId\":1");
    assertThat(serialized).contains("\"metricId\":2");
  }

  @Test
  @SneakyThrows
  void shouldDeserializeMetric() {
    var json = "{\"metricId\":1,\"value\":3}";
    var datapoint = objectMapper.readValue(json, MetricDatapoint.class);

    assertThat(datapoint).isInstanceOf(MetricDatapoint.class);
    assertThat(datapoint.value()).isEqualTo(3);
    assertThat(datapoint.metricId()).isEqualTo(1);
  }
}

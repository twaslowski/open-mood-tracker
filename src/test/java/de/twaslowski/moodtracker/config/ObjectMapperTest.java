package de.twaslowski.moodtracker.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class ObjectMapperTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @SneakyThrows
  void shouldSerializeEmptyMetric() {
    var mood = new MetricDatapoint(Mood.NAME, null);
    var json = "{\"metricName\":\"MOOD\",\"value\":null}";

    assertThat(objectMapper.writeValueAsString(mood)).isEqualTo(json);
  }

  @Test
  @SneakyThrows
  void shouldSerializePopulatedMetric() {
    var mood = new MetricDatapoint(Sleep.NAME, 8);
    var json = "{\"metricName\":\"SLEEP\",\"value\":8}";

    assertThat(objectMapper.writeValueAsString(mood)).isEqualTo(json);
  }

  @Test
  @SneakyThrows
  void shouldSerializeListOfMetrics() {
    var metrics = List.of(
        new MetricDatapoint(Mood.NAME, 3),
        new MetricDatapoint(Sleep.NAME, 8)
    );

    var serialized = objectMapper.writeValueAsString(metrics);
    assertThat(serialized).contains("\"metricName\":\"MOOD\"");
    assertThat(serialized).contains("\"metricName\":\"SLEEP\"");
  }

  @Test
  @SneakyThrows
  void shouldDeserializeMetric() {
    var json = "{\"metricName\":\"MOOD\",\"value\":3}";
    var datapoint = objectMapper.readValue(json, MetricDatapoint.class);

    assertThat(datapoint).isInstanceOf(MetricDatapoint.class);
    assertThat(datapoint.value()).isEqualTo(3);
    assertThat(datapoint.metricName()).isEqualTo(Mood.NAME);
  }
}

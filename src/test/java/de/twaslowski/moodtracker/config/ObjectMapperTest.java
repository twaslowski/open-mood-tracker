package de.twaslowski.moodtracker.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class ObjectMapperTest {

  private final ObjectMapper objectMapper = new ObjectMapperConfig().objectMapper();

  @Test
  @SneakyThrows
  void shouldSerializeEmptyMetric() {
    var mood = Mood.empty();
    var json = "{\"type\":\"MOOD\",\"value\":null}";

    assertThat(objectMapper.writeValueAsString(mood)).isEqualTo(json);
  }

  @Test
  @SneakyThrows
  void shouldSerializePopulatedMetric() {
    var mood = Sleep.of(8);
    var json = "{\"type\":\"SLEEP\",\"value\":8}";

    assertThat(objectMapper.writeValueAsString(mood)).isEqualTo(json);
  }

  @Test
  @SneakyThrows
  void shouldDeserializeMetric() {
    var json = "{\"type\":\"MOOD\",\"value\":3}";
    var mood = objectMapper.readValue(json, Metric.class);

    assertThat(mood).isInstanceOf(Mood.class);
    assertThat(mood.getValue()).isEqualTo(3);
  }

  @Test
  @SneakyThrows
  void shouldSerializeListOfMetrics() {
    var metrics = List.of(Mood.empty(), Sleep.empty());

    var serialized = objectMapper.writeValueAsString(metrics);
    assertThat(serialized).contains("\"type\":\"MOOD\"");
    assertThat(serialized).contains("\"type\":\"SLEEP\"");
    assertThat(serialized).contains("\"value\":null");
  }
}

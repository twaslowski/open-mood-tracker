package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CallbackGenerator {

  private final ObjectMapper objectMapper;

  public LinkedHashMap<String, String> createCallbacks(Metric metric) {
    var orderedCallbacks = getCallbackMappingForMetric(metric);

    LinkedHashMap<String, String> callbacks = new LinkedHashMap<>();
    for (var entry : orderedCallbacks.entrySet()) {
      callbacks.put(entry.getValue(), safeWriteValueAsString(entry.getKey()));
    }
    return callbacks;
  }

  private Map<Metric, String> getCallbackMappingForMetric(Metric metric) {
    TreeMap<Metric, String> sortedMap;

    switch (metric.getName()) {
      case Mood.TYPE -> {
        sortedMap = new TreeMap<>(MetricComparator.descending());
        sortedMap.putAll(Mood.LABELS);
      }
      case Sleep.TYPE -> {
        sortedMap = new TreeMap<>(MetricComparator.ascending());
        sortedMap.putAll(Sleep.LABELS);
      }
      default -> throw new IllegalArgumentException("Unknown type: " + metric.getName());
    }
    return sortedMap;
  }

  @SneakyThrows
  private String safeWriteValueAsString(Metric metric) {
    return objectMapper.writeValueAsString(metric);
  }
}

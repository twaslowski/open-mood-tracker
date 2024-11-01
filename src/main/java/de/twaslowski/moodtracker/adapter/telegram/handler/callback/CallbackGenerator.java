package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
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

  private Map<MetricDatapoint, String> getCallbackMappingForMetric(Metric metric) {
    TreeMap<MetricDatapoint, String> sortedMap = new TreeMap<>(metric.getComparator());

    for (Entry<Integer, String> entry : metric.getLabels().entrySet()) {
      sortedMap.put(
          metric.datapointWithValue(entry.getKey()),
          entry.getValue()
      );
    }
    return sortedMap;
  }

  @SneakyThrows
  private String safeWriteValueAsString(MetricDatapoint metric) {
    return objectMapper.writeValueAsString(metric);
  }
}

package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiParser;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MetricCallbackGenerator {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public LinkedHashMap<String, String> createCallbacks(Metric metric) {
    var orderedCallbacks = getCallbackMappingForMetric(metric);

    LinkedHashMap<String, String> callbacks = new LinkedHashMap<>();
    for (var entry : orderedCallbacks.entrySet()) {
      callbacks.put(entry.getValue(), objectMapper.writeValueAsString(entry.getKey()));
    }
    return callbacks;
  }

  private Map<MetricDatapoint, String> getCallbackMappingForMetric(Metric metric) {
    TreeMap<MetricDatapoint, String> sortedMap = new TreeMap<>(metric.getComparator());

    for (Entry<Integer, String> entry : metric.getLabels().entrySet()) {
      sortedMap.put(
          metric.datapointWithValue(entry.getKey()),
          EmojiParser.parseToUnicode(entry.getValue())
      );
    }
    return sortedMap;
  }
}

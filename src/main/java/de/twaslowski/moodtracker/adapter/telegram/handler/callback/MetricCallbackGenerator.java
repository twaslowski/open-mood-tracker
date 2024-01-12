package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.domain.entity.Metric;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MetricCallbackGenerator {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public CallbackContainer createCallbacks(Metric metric) {
    return CallbackContainer.builder()
        .callbacks(callbackForMetric(metric))
        .comparator(Comparator.comparing(Callback::getText))
        .build();
  }

  private List<Callback> callbackForMetric(Metric metric) {
    return metric.getLabels().entrySet().stream()
        .map(entry -> new Callback(entry.getValue(), unsafeWrite(metric, entry.getKey())))
        .collect(Collectors.toList());
  }

  @SneakyThrows
  private String unsafeWrite(Metric metric, Integer value) {
    return objectMapper.writeValueAsString(metric.datapointWithValue(value));
  }
}

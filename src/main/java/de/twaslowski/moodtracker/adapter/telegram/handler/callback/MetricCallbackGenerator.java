package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.Callback;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.Metric.SortOrder;
import de.twaslowski.moodtracker.domain.value.LabelComparator;
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
  public List<Callback> createCallbacks(Metric metric) {
    return callbackForMetric(metric);
  }

  private List<Callback> callbackForMetric(Metric metric) {
    var callbacks = metric.getLabels().stream()
        .sorted(new LabelComparator())
        .map(label -> new Callback(label.label(), unsafeWrite(metric, label.value())))
        .collect(Collectors.toList());

    if (metric.getSortOrder() == SortOrder.DESC) {
      return callbacks.reversed();
    }
    return callbacks;
  }

  @SneakyThrows
  private String unsafeWrite(Metric metric, Integer value) {
    return objectMapper.writeValueAsString(metric.datapointWithValue(value));
  }
}

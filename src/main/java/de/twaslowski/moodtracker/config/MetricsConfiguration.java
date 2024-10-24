package de.twaslowski.moodtracker.config;

import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

  /**
   * Defines a list of Metrics to be recorded IN ORDER via a LinkedHashMap that is autowired
   * to the RecordService.
   */

  private static final List<Metric> METRICS = new ArrayList<>(List.of(
      new Mood(),
      new Sleep()
  ));

  @Bean
  public LinkedHashMap<String, Metric> defaultMetrics() {
    LinkedHashMap<String, Metric> metrics = new LinkedHashMap<>();
    for (var metric : METRICS) {
      metrics.put(metric.getName(), metric);
    }
    return metrics;
  }

  @Bean
  public Set<MetricDatapoint> defaultBaselineConfiguration(Map<String, Metric> defaultMetrics) {
    return defaultMetrics.values().stream()
        .map(MetricDatapoint::fromMetricDefault)
        .collect(Collectors.toSet());
  }
}

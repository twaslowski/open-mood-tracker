package de.twaslowski.moodtracker.config;

import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.util.LinkedHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MetricsConfiguration {

  /**
   * Defines a list of Metrics to be recorded IN ORDER via a LinkedHashMap that is autowired
   * to the RecordService.
   */

  private final MetricRepository metricRepository;

  @Getter
  @RequiredArgsConstructor
  public enum DefaultMetrics {
    MOOD(Mood.INSTANCE),
    SLEEP(Sleep.INSTANCE);

    private final Metric metric;
  }

  @Bean
  public void seedDatabase() {
    for (var defaultMetric : DefaultMetrics.values()) {
      var metric = defaultMetric.getMetric();
      if (metricRepository.findByNameAndOwnerId(metric.getName(), metric.getOwnerId()).isEmpty()) {
        metricRepository.save(metric);
      }
    }
  }

  public static LinkedHashMap<String, Metric> defaultMetrics() {
    LinkedHashMap<String, Metric> metrics = new LinkedHashMap<>();
    for (var metric : DefaultMetrics.values()) {
      metrics.put(metric.name(), metric.getMetric());
    }
    return metrics;
  }
}

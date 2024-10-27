package de.twaslowski.moodtracker.config;

import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MetricConfiguration {

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
  public List<Metric> defaultMetrics() {
    return Arrays.stream(DefaultMetrics.values())
        .map(DefaultMetrics::getMetric)
        .map(this::getOrCreate)
        .toList();
  }

  private Metric getOrCreate(Metric metric) {
    return metricRepository.findByName(metric.getName())
        .orElseGet(() -> metricRepository.save(metric));
  }

  public List<MetricDatapoint> defaultBaselineConfiguration() {
    return defaultMetrics().stream()
        .map(Metric::defaultDatapoint)
        .toList();
  }
}

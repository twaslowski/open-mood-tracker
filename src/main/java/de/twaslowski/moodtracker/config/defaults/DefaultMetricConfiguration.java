package de.twaslowski.moodtracker.config.defaults;

import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DefaultMetricConfiguration {

  private final MetricRepository metricRepository;

  @Getter
  @RequiredArgsConstructor
  public enum DefaultMetrics {
    // The INSTANCE constructs are used only for initialization and testing purposes
    MOOD(MoodMetric.INSTANCE),
    SLEEP(SleepMetric.INSTANCE);

    private final Metric metric;
  }

  @Bean
  public List<Metric> defaultMetrics() {
    return Arrays.stream(DefaultMetrics.values())
        .map(DefaultMetrics::getMetric)
        .map(this::createOrUpdate)
        .toList();
  }

  @Bean
  public List<MetricDatapoint> defaultBaselineConfiguration() {
    return defaultMetrics().stream()
        .map(Metric::defaultDatapoint)
        .toList();
  }

  private Metric createOrUpdate(Metric metric) {
    // If the metric is already present in the database, update it; otherwise, create a new one
    return metricRepository.findByName(metric.getName())
        .map(metricRepository::save)
        .orElse(metricRepository.save(metric));
  }
}

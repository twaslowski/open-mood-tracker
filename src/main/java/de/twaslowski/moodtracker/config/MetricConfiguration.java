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

  private final MetricRepository metricRepository;

  @Getter
  @RequiredArgsConstructor
  public enum DefaultMetrics {
    // The INSTANCE constructs are used only for initialization and testing purposes
    MOOD(Mood.INSTANCE),
    SLEEP(Sleep.INSTANCE);

    private final Metric metric;
  }

  @Bean
  public List<Metric> defaultMetrics() {
    return Arrays.stream(DefaultMetrics.values())
        .map(DefaultMetrics::getMetric)
        .map(this::createOrUpdate)
        .toList();
  }

  private Metric createOrUpdate(Metric metric) {
    // If the metric is already present in the database, update it; otherwise, create a new one
    return metricRepository.findByName(metric.getName())
        .map(metricRepository::save)
        .orElse(metricRepository.save(metric));
  }

  public List<MetricDatapoint> defaultBaselineConfiguration() {
    return defaultMetrics().stream()
        .map(Metric::defaultDatapoint)
        .toList();
  }
}

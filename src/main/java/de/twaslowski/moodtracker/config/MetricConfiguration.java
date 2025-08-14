package de.twaslowski.moodtracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class MetricConfiguration {

  private final String configPath;
  private final ObjectMapper metricMapper;
  private final MetricRepository metricRepository;
  private final ParsedMetricConfiguration parsedMetricConfiguration;

  public MetricConfiguration(@Value("${mood-tracker.metrics.config-path}") String configPath,
                             @Qualifier("metricMapper") ObjectMapper metricMapper,
                             MetricRepository metricRepository) {
    this.configPath = configPath;
    this.metricRepository = metricRepository;
    this.metricMapper = metricMapper;
    this.parsedMetricConfiguration = loadMetricsFromConfiguration();
  }

  @Bean
  @Transactional
  public List<Metric> defaultMetrics() {
    List<Metric> savedMetrics = new ArrayList<>();
    for (Metric metric : parsedMetricConfiguration.defaults()) {
      metric.setDefaultMetric(true);

      Optional<Metric> existingMetric = metricRepository.findMetricsByDefaultMetricIsTrueAndNameEquals(metric.getName());

      if (existingMetric.isPresent()) {
        Metric existing = existingMetric.get();
        existing.updateWith(metric);
        savedMetrics.add(metricRepository.save(existing));
      } else {
        savedMetrics.add(metricRepository.save(metric));
      }
    }
    return savedMetrics;
  }

  @Bean
  public List<Metric> defaultTrackedMetrics(List<Metric> defaultMetrics) {
    return defaultMetrics.stream()
        .filter(metric -> parsedMetricConfiguration.tracked().contains(metric.getName()))
        .toList();
  }

  @SneakyThrows
  public ParsedMetricConfiguration loadMetricsFromConfiguration() {
    return metricMapper.readValue(new File(configPath), ParsedMetricConfiguration.class);
  }

  public record ParsedMetricConfiguration(
      List<Metric> defaults,
      List<String> tracked
  ) {

  }
}


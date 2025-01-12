package de.twaslowski.moodtracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.io.File;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfiguration {

  private final String configPath;
  private final ObjectMapper metricMapper;
  private final MetricRepository metricRepository;

  public MetricConfiguration(@Value("${mood-tracker.metrics.config-path}") String configPath,
                             @Qualifier("metricMapper") ObjectMapper metricMapper,
                             MetricRepository metricRepository) {
    this.configPath = configPath;
    this.metricRepository = metricRepository;
    this.metricMapper = metricMapper;
  }

  @Bean
  public List<Metric> defaultMetrics() {
    var configuredMetrics = loadMetricsFromConfiguration();
    metricRepository.saveAll(configuredMetrics.defaults);
    return configuredMetrics.defaults.stream()
        .filter(metric -> configuredMetrics.tracked.contains(metric.getName()))
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

package de.twaslowski.moodtracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.repository.MetricRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsProvider {

  private final String configPath;
  private final ObjectMapper metricMapper;
  private final MetricRepository metricRepository;

  public MetricsProvider(@Value("${mood-tracker.metrics.config-path}") String configPath,
                         @Qualifier("metricMapper") ObjectMapper metricMapper,
                         MetricRepository metricRepository) {
    this.configPath = configPath;
    this.metricRepository = metricRepository;
    this.metricMapper = metricMapper;
  }

  @SneakyThrows
  public void loadMetrics() {
    var metrics = metricMapper.readValue(configPath, Metric[].class);
  }
}

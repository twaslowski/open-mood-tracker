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

  @Bean
  public List<Metric> defaultMetrics() {
    return metricRepository.saveAll(loadMetricsFromConfiguration());
  }

  @SneakyThrows
  public List<Metric> loadMetricsFromConfiguration() {
    var metricCollectionType = metricMapper.getTypeFactory().constructCollectionType(List.class, Metric.class);
    return metricMapper.readValue(new File(configPath), metricCollectionType);
  }
}

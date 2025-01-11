package de.twaslowski.moodtracker.config.defaults;

import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DefaultMetricConfiguration {

  private final List<Metric> defaultMetrics;

  @Bean
  public List<MetricDatapoint> defaultBaselineConfiguration() {
    return defaultMetrics.stream()
        .map(Metric::defaultDatapoint)
        .toList();
  }
}

package de.twaslowski.moodtracker.config;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class MetricsProviderTest {

  @Autowired
  private MetricConfiguration metricsProvider;

  @Test
  void shouldLoadMetricsFromConfiguration() {
    metricsProvider.loadMetricsFromConfiguration();
  }
}

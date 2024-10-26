package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MetricService {

  private final MetricRepository metricRepository;

  public List<String> getDefaultMetricNames() {
    // todo this should be autowired from the MetricsConfiguration
    return List.of(Mood.NAME, Sleep.NAME);
  }

  public Metric getMetricByName(String name) {
    return metricRepository.findByName(name)
        // todo custom exception type
        .orElseThrow(() -> new IllegalArgumentException("Unknown metric: " + name));
  }

  // todo this should be coupled to the autowired MetricsConfiguration as well
  public List<MetricDatapoint> getDefaultBaselineConfiguration() {
    return List.of(
        MetricDatapoint.defaultForMetric(Mood.INSTANCE),
        MetricDatapoint.defaultForMetric(Sleep.INSTANCE)
    );
  }
}

package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.config.MetricConfiguration;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.exception.MetricNotFoundException;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MetricService {

  private final MetricRepository metricRepository;
  private final MetricConfiguration metricConfiguration;

  public List<Metric> getDefaultMetrics() {
    return metricConfiguration.defaultMetrics();
  }

  public Metric getMetricByName(String name) {
    return metricRepository.findByName(name)
        .orElseThrow(() -> new MetricNotFoundException("Could not find Metric with name: " + name));
  }

  public Metric getMetricById(long id) {
    return metricRepository.findById(id)
        .orElseThrow(() -> new MetricNotFoundException("Could not find Metric with id: " + id));
  }

  public List<MetricDatapoint> getDefaultBaselineConfiguration() {
    return metricConfiguration.defaultBaselineConfiguration();
  }
}

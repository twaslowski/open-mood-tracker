package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.exception.MetricNotFoundException;
import de.twaslowski.moodtracker.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MetricService {

  private final MetricRepository metricRepository;

  public Metric getMetricById(long id) {
    return metricRepository.findById(id)
        .orElseThrow(() -> new MetricNotFoundException("Could not find Metric with id: " + id));
  }

  public Metric saveMetric(Metric metric) {
    return metricRepository.save(metric);
  }
}

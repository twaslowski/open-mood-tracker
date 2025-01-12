package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.exception.MetricNotFoundException;
import de.twaslowski.moodtracker.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MetricService {

  // This class looks like it could be deleted now; however, it will hold functionality
  // for editing defaults when that feature becomes available.

  private final MetricRepository metricRepository;

  public Metric getMetricById(long id) {
    return metricRepository.findById(id)
        .orElseThrow(() -> new MetricNotFoundException("Could not find Metric with id: " + id));
  }

  public String getMetricName(long id) {
    return getMetricById(id).getName();
  }
}

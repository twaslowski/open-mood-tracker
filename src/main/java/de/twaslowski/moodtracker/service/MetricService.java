package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.exception.MetricNotFoundException;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.util.List;
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

  public List<Metric> findUserMetrics(String userId) {
    return metricRepository.findMetricsByOwnerId(userId);
  }

  public List<Metric> findDefaultMetrics() {
    return metricRepository.findMetricsByDefaultMetricIsTrue();
  }
}

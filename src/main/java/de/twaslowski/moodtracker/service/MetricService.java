package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.domain.dto.MetricDTO;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.exception.MetricAlreadyTrackedException;
import de.twaslowski.moodtracker.exception.MetricNotFoundException;
import de.twaslowski.moodtracker.exception.MetricNotTrackedException;
import de.twaslowski.moodtracker.exception.MetricOwnerMismatchException;
import de.twaslowski.moodtracker.repository.MetricConfigurationRepository;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MetricService {

  private final MetricRepository metricRepository;
  private final MetricConfigurationRepository metricConfigurationRepository;

  public Metric getMetricById(long id) {
    return metricRepository.findById(id)
        .orElseThrow(() -> new MetricNotFoundException("Could not find Metric with id: " + id));
  }

  public MetricConfiguration trackMetric(long metricId, User user) {
    Metric metric = getMetricById(metricId);
    if (!metric.isDefaultMetric() && !user.getId().equals(metric.getOwnerId())) {
      throw new IllegalArgumentException("User does not own metric");
    }
    if (metricConfigurationRepository.existsByMetricIdAndUserId(metricId, user.getId())) {
      throw new MetricAlreadyTrackedException(user.getId(), metricId);
    }
    return metricConfigurationRepository.save(MetricConfiguration.from(metric, user));
  }

  public void removeMetricTracking(String trackedMetricId, User user) {
    var trackedMetric = metricConfigurationRepository.findById(trackedMetricId)
        .filter(m -> m.getUser().getId().equals(user.getId()))
        .orElseThrow(() -> new MetricNotTrackedException(user.getId(), trackedMetricId));
    metricConfigurationRepository.delete(trackedMetric);
  }

  @Transactional
  public List<MetricDTO> findUserMetrics(String userId) {
    List<Metric> metrics = metricRepository.findMetricsByDefaultMetricIsTrueOrOwnerIdEquals(userId);
    List<MetricConfiguration> metricConfigurations = metricConfigurationRepository.findTrackedMetricsByUserId(userId);
    List<MetricDTO> trackedMetricDTOs = metricConfigurations.stream()
        .map(MetricDTO::from)
        .toList();

    List<MetricDTO> untrackedMetricDTOs = metrics.stream()
        .filter(metric -> metricConfigurations.stream()
            .noneMatch(trackedMetric -> trackedMetric.getMetric().getId() == metric.getId()))
        .map(MetricDTO::from)
        .toList();

    List<MetricDTO> consolidatedMetrics = new ArrayList<>();
    consolidatedMetrics.addAll(trackedMetricDTOs);
    consolidatedMetrics.addAll(untrackedMetricDTOs);
    return consolidatedMetrics;
  }

  public Metric createMetric(User user, MetricDTO metricDTO) {
    if (!metricDTO.validateLabels()) {
      throw new IllegalArgumentException("Metric labels are not valid");
    }
    var metric = Metric.from(metricDTO, user);

    if (metricDTO.tracked()) {
      var trackedMetric = MetricConfiguration.from(metric, user);
      metricConfigurationRepository.save(trackedMetric);
    }

    metricRepository.save(metric);
    return metric;
  }

  public Metric updateMetric(User user, MetricDTO metricDTO) {
    var existingMetric = getMetricById(metricDTO.id());
    var trackedMetric = metricConfigurationRepository.findByUserIdAndMetricId(user.getId(), metricDTO.id());
    if (existingMetric.isDefaultMetric()) {
      return cloneDefaultMetric(user, metricDTO);
    } else {
      if (!user.getId().equals(existingMetric.getOwnerId())) {
        throw new MetricOwnerMismatchException(user.getId(), existingMetric.getId());
      }
      if (!metricDTO.validateLabels()) {
        throw new IllegalArgumentException("Metric labels are not valid");
      }
      return metricRepository.save(existingMetric.updateWith(metricDTO));
    }
  }

  private Metric cloneDefaultMetric(User user, MetricDTO metricDTO) {
    var newMetric = Metric.from(metricDTO, user);
    return metricRepository.save(newMetric);
  }
}

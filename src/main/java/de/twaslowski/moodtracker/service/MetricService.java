package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.domain.dto.MetricDTO;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.exception.MetricNotFoundException;
import de.twaslowski.moodtracker.exception.MetricNotTrackedException;
import de.twaslowski.moodtracker.repository.MetricConfigurationRepository;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MetricService {

  private final MetricRepository metricRepository;
  private final MetricConfigurationRepository metricConfigurationRepository;

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

  public Metric getMetricById(long id) {
    return metricRepository.findById(id)
        .orElseThrow(() -> new MetricNotFoundException("Could not find Metric with id: " + id));
  }

  public MetricConfiguration trackMetric(long metricId, User user) {
    Metric metric = getMetricById(metricId);
    return metricConfigurationRepository.findByUserIdAndMetricId(user.getId(), metricId)
        .map(m -> m.validateOwnership(user))
        .map(MetricConfiguration::track)
        .map(metricConfigurationRepository::save)
        .orElseGet(() -> metricConfigurationRepository.save(MetricConfiguration.from(metric, user)));
  }

  public void untrackMetric(long metricId, User user) {
    var trackedMetric = metricConfigurationRepository.findByUserIdAndMetricId(user.getId(), metricId)
        .map(metricConfiguration -> metricConfiguration.validateOwnership(user))
        .map(metricConfiguration -> {
          metricConfiguration.setTracked(false);
          return metricConfiguration;
        })
        .orElseThrow(() -> new MetricNotTrackedException(user.getId(), metricId));
    metricConfigurationRepository.save(trackedMetric);
  }

  public MetricConfiguration updateMetricConfiguration(User user, MetricDTO metricDTO) {
    var existingMetric = getMetricById(metricDTO.id());
    return metricConfigurationRepository.findByUserIdAndMetricId(user.getId(), metricDTO.id())
        .map(m -> m.validateOwnership(user))
        .map(m -> m.updateWith(metricDTO))
        .map(metricConfigurationRepository::save)
        .orElseGet(() -> {
          MetricConfiguration newConfiguration = MetricConfiguration.from(existingMetric, user);
          newConfiguration.updateWith(metricDTO);
          return metricConfigurationRepository.save(newConfiguration);
        });
  }
}

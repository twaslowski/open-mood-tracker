package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricConfigurationRepository extends JpaRepository<MetricConfiguration, String> {

  List<MetricConfiguration> findByUserId(String id);

  List<MetricConfiguration> findTrackedMetricsByUserId(String id);

  Optional<MetricConfiguration> findByUserIdAndMetricId(String userId, long metricId);
}

package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.domain.entity.Metric;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRepository extends JpaRepository<Metric, Long> {

  Optional<Metric> findByName(String name);

  List<Metric> findMetricsByDefaultMetricIsTrueOrOwnerIdEquals(String ownerId);

  Optional<Metric> findMetricsByDefaultMetricIsTrueAndNameEquals(String name);
}

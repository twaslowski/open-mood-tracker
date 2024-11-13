package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.domain.entity.Metric;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRepository extends JpaRepository<Metric, Long> {

  Optional<Metric> findByName(String name);
}

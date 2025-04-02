package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.domain.entity.TrackedMetric;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackedMetricRepository extends JpaRepository<TrackedMetric, String> {

  List<TrackedMetric> findByUserId(String id);
}

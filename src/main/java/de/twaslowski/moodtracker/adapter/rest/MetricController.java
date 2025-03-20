package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.repository.MetricRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MetricController {

  private final MetricRepository metricRepository;

  @GetMapping("/metric")
  public List<Metric> getUserMetrics() {
    return metricRepository.findAll();
  }

  @PostMapping("/metric")
  public ResponseEntity<Metric> createMetric(Metric metric) {
    return ResponseEntity.ok(metricRepository.save(metric));
  }

  @PutMapping("/metric/{id}")
  public ResponseEntity<Metric> updateMetric(@PathVariable Long id, Metric metric) {
    return ResponseEntity.ok(metricRepository.save(metric));
  }

  @DeleteMapping("/metric/{id}")
  public ResponseEntity<?> deleteAllMetrics(@PathVariable Long id) {
    metricRepository.deleteAll();
    return ResponseEntity.status(204).build();
  }
}

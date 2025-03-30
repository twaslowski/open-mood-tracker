package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.service.MetricService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MetricController {

  private final MetricService metricService;

  @GetMapping("/metrics")
  public List<Metric> getUserMetrics(@AuthenticationPrincipal User user) {
    log.info("Retrieving metrics for user {}", user.getId());
    var customMetrics = metricService.findUserMetrics(user.getId());
    var defaultMetrics = metricService.findDefaultMetrics();
    customMetrics.addAll(defaultMetrics);
    return customMetrics;
  }

  @PostMapping("/metric")
  public ResponseEntity<Metric> createMetric(Metric metric) {
    return ResponseEntity.ok().build();
  }

  @PutMapping("/metric/{id}")
  public ResponseEntity<Metric> updateMetric(@PathVariable Long id, Metric metric) {
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/metric/{id}")
  public ResponseEntity<?> deleteAllMetrics(@PathVariable Long id) {
    return ResponseEntity.status(204).build();
  }
}

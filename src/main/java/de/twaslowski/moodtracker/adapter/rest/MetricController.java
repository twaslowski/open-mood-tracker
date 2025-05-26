package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.domain.dto.MetricDTO;
import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MetricController {

  private final MetricService metricService;

//  @PostMapping("/metric")
//  public ResponseEntity<MetricDTO> createMetric(@AuthenticationPrincipal User user,
//                                                @RequestBody MetricDTO metricDTO) {
//    log.info("Creating metric for user {}", user.getId());
//    Metric metric = metricService.createMetric(user, metricDTO);
//    return ResponseEntity.ok(MetricDTO.from(metric));
//  }

  @PutMapping("/metric")
  public ResponseEntity<MetricDTO> updateMetric(@AuthenticationPrincipal User user,
                                                @RequestBody MetricDTO metricDTO) {
    log.info("Updating metric {} for user {}", metricDTO.id(), user.getId());
    MetricConfiguration updatedMetricConfiguration = metricService.updateMetricConfiguration(user, metricDTO);
    return ResponseEntity.ok(MetricDTO.from(updatedMetricConfiguration));
  }

  @GetMapping("/metric")
  public ResponseEntity<List<MetricDTO>> getUserMetrics(@AuthenticationPrincipal User user) {
    log.info("Retrieving metrics for user {}", user.getId());
    return ResponseEntity.ok(metricService.findUserMetrics(user.getId()));
  }

  @PostMapping("/metric/tracking/{metricId}")
  public ResponseEntity<MetricDTO> trackMetric(@PathVariable long metricId, @AuthenticationPrincipal User user) {
    log.info("Tracking metric {} for user {}", metricId, user.getId());
    var trackedMetric = metricService.trackMetric(metricId, user);
    return ResponseEntity.ok(MetricDTO.from(trackedMetric));
  }

  @DeleteMapping("/metric/tracking/{metricId}")
  public ResponseEntity<MetricConfiguration> untrackMetric(@PathVariable long metricId, @AuthenticationPrincipal User user) {
    log.info("Removing metric tracking {} for user {}", metricId, user.getId());
    metricService.untrackMetric(metricId, user);
    return ResponseEntity.status(204).build();
  }
}

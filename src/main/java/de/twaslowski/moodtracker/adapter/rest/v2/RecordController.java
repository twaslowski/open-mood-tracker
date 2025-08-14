package de.twaslowski.moodtracker.adapter.rest.v2;

import de.twaslowski.moodtracker.adapter.rest.v2.dto.Datapoint;
import de.twaslowski.moodtracker.adapter.rest.v2.dto.MetricSeries;
import de.twaslowski.moodtracker.domain.entity.Record;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.service.MetricService;
import de.twaslowski.moodtracker.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController(value = "RecordControllerV2")
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class RecordController {

  private final RecordService recordService;
  private final MetricService metricService;

  @GetMapping("/records")
  @Transactional(readOnly = true)
  public ResponseEntity<Collection<MetricSeries>> getRecords(@AuthenticationPrincipal User user,
                                                             @RequestParam(required = false) ZonedDateTime from,
                                                             @RequestParam(required = false) ZonedDateTime to
  ) {
    log.info("Getting records for user {}", user.getId());

    // Fetch all metrics tracked by the user and all records
    var trackedMetrics = metricService.findUserMetrics(user.getId());
    var records = recordService.getRecords(user.getId()).stream()
        .sorted(Comparator.comparing(record -> record.getCreationTimestamp().toInstant()))
        .toList();

    Map<Long, MetricSeries> seriesMap = trackedMetrics.stream()
        .collect(Collectors.toMap(
            metric -> metric.getMetric().getId(),
            MetricSeries::from
        ));

    log.info("Found {} records for user {}. Generating time series for metrics {}.",
        records.size(), user.getId(), seriesMap.keySet());

    // Fetch all datapoints from each record and add them to their respective time series.
    for (Record record : records) {
      record.getValues().forEach(value -> {
            var metricSeries = seriesMap.get(value.metricId());
            if (metricSeries == null) {
              log.warn("No metric series found for metric ID {} in record {}", value.metricId(), record.getId());
              return;
            }
            metricSeries.add(Datapoint.of(record.getCreationTimestamp(), value.value()));
          }
      );
    }

    return ResponseEntity.ok(seriesMap.values());
  }
}

package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.Record;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import de.twaslowski.moodtracker.repository.RecordRepository;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordService {

  private final RecordRepository recordRepository;
  private final MetricService metricService;
  private final UserService userService;

  public Record initializeFrom(User user) {
    var configuration = userService.getUserConfiguration(user);
    var initialDatapoints = configuration.getTrackedMetricIds().stream()
        .map(metricService::getMetricById)
        .map(Metric::emptyDatapoint)
        .toList();

    var record = Record.builder()
        .userId(user.getId())
        .values(initialDatapoints)
        .build();

    return recordRepository.save(record);
  }

  public void recordFromBaseline(User user) {
    var baselineConfiguration = userService.getBaselineConfiguration(user.getId());
    recordRepository.save(
        Record.builder()
            .userId(user.getId())
            .values(baselineConfiguration)
            .build()
    );
  }

  public Optional<Record> findIncompleteRecordsForUser(String userId) {
    var incompleteRecords = recordRepository.findByUserId(userId).stream()
        .filter(Record::hasIncompleteMetric)
        .collect(Collectors.toSet());

    if (incompleteRecords.size() > 1) {
      log.warn("Found multiple incomplete records for chatId: {}", userId);
    }

    return incompleteRecords.stream()
        .max(Comparator.comparing(Record::getCreationTimestamp));
  }

  public Optional<Metric> getNextIncompleteMetric(Record record) {
    return record.getIncompleteMetricIds().stream()
        .map(metricService::getMetricById)
        .findFirst();
  }

  public String stringifyRecord(Record record) {
    var result = new StringBuilder();
    for (MetricDatapoint datapoint : record.getValues()) {
      var metric = metricService.getMetricById(datapoint.metricId());
      result.append(metric.getName());
      result.append(": ");
      result.append(metric.getLabels().get(datapoint.value()));
      result.append("\n");
    }
    return result.toString();
  }
}

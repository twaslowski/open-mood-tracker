package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.entity.Record;
import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
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
  private final UserService userService;

  public Record initializeFrom(User user) {
    var initialDatapoints = user.getConfiguration().getMetrics().stream()
        .map(MetricDatapoint::forMetric)
        .toList();

    var record = Record.builder()
        .userId(user.getId())
        .values(initialDatapoints)
        .build();

    return recordRepository.save(record);
  }

  // todo kept for backwards compatibility, remove
  public Record initializeFrom(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());
    var initialDatapoints = user.getConfiguration().getMetrics().stream()
        .map(MetricDatapoint::forMetric)
        .toList();

    var record = Record.builder()
        .userId(user.getId())
        .values(initialDatapoints)
        .build();

    return recordRepository.save(record);
  }

  public void fromBaselineConfiguration(User user) {
    recordRepository.save(
        Record.builder()
            .userId(user.getId())
            .values(user.getBaselineConfiguration())
            .build()
    );
  }

  public Optional<Record> findIncompleteRecordsForUser(long chatId) {
    var incompleteRecords = recordRepository.findByUserId(chatId).stream()
        .filter(Record::hasIncompleteMetric)
        .collect(Collectors.toSet());

    if (incompleteRecords.size() > 1) {
      log.warn("Found multiple incomplete records for chatId: {}", chatId);
    }

    return incompleteRecords.stream()
        .max(Comparator.comparing(Record::getCreationTimestamp));
  }

  public Optional<Metric> getNextIncompleteMetric(Record record) {
    // Returns the next incomplete metric IN ORDER according to the Order of the Metrics bean
    var user = userService.findByTelegramId(record.getUserId());
    var incompleteMetricNames = record.getIncompleteMetrics().stream()
        .map(MetricDatapoint::metricName)
        .toList();
    for (Metric metric : user.getConfiguration().getMetrics()) {
      if (incompleteMetricNames.contains(metric.getName())) {
        return Optional.of(metric);
      }
    }
    return Optional.empty();
  }
}

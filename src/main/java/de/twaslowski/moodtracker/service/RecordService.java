package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.entity.Record;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import de.twaslowski.moodtracker.repository.RecordRepository;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {

  private final RecordRepository recordRepository;
  private final LinkedHashMap<String, Metric> metrics;

  public Record initializeFrom(TelegramUpdate update) {
    var record = Record.builder()
        .telegramId(update.getChatId())
        .values(Set.of(
                MetricDatapoint.forMetric(Mood.TYPE),
                MetricDatapoint.forMetric(Sleep.TYPE)
            )
        )
        .build();

    return recordRepository.save(record);
  }

  public Optional<Record> findIncompleteRecordForTelegramChat(long chatId) {
    return recordRepository.findByTelegramId(chatId).stream()
        .filter(Record::hasIncompleteMetric)
        .findFirst();
  }

  public Metric getNextIncompleteMetric(Record record) {
    // Returns the next incomplete metric IN ORDER according to the Order of the Metrics bean
    var incompleteMetricNames = record.getIncompleteMetrics().stream()
        .map(MetricDatapoint::metricName)
        .toList();
    for (Metric metric : metrics.sequencedValues()) {
      if (incompleteMetricNames.contains(metric.getName())) {
        return metric;
      }
    }
    throw new IllegalStateException("No incomplete metric found.");
  }

  public Record store(Record record) {
    return recordRepository.save(record);
  }
}

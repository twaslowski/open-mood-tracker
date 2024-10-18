package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.entity.Record;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import de.twaslowski.moodtracker.repository.RecordRepository;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {

  private final RecordRepository recordRepository;

  // todo using Set like this is problematic because there is no order.
  // I have to either use List and deal with serialization issues, or find a way to order the metrics.
  // Probably we can just define an order somewhere in the configuration and adhere to it.
  // Dealing with duplicate metric values will work as intended, but the following records can always
  // be sent out based on the first unanswered record from the pre-defined order.
  public Record initializeFrom(TelegramUpdate update) {
    var record = Record.builder()
        .telegramId(update.getChatId())
        .values(Set.of(Mood.empty(), Sleep.empty()))
        .build();
    return recordRepository.save(record);
  }

  public Optional<Record> findIncompleteRecordForTelegramChat(long chatId) {
    return recordRepository.findByTelegramId(chatId).stream()
        .filter(Record::hasIncompleteMetric)
        .findFirst();
  }

  public Record store(Record record) {
    return recordRepository.save(record);
  }
}

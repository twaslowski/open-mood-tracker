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

  public void initializeFrom(TelegramUpdate update) {
    var record = Record.builder()
        .telegramId(update.getChatId())
        .values(Set.of(Mood.empty(), Sleep.empty()))
        .build();
    recordRepository.save(record);
  }

  public Optional<Record> findIncompleteRecordForTelegramChat(long chatId) {
    return recordRepository.findByTelegramId(chatId).stream()
        .filter(Record::hasIncompleteMetric)
        .findFirst();
  }

  public void store(Record record) {
    recordRepository.save(record);
  }
}

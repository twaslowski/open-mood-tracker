package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.entity.Record;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.repository.RecordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {

  private final RecordRepository recordRepository;

  public Record initializeFrom(TelegramUpdate update) {
    var record = Record.builder()
        .telegramId(update.getChatId())
        .values(List.of(Mood.empty()))
        .build();
    return recordRepository.save(record);
  }
}

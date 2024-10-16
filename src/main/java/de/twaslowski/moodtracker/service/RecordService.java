package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.entity.Record;
import de.twaslowski.moodtracker.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {

  private final RecordRepository recordRepository;

  public Record createFrom(TelegramUpdate update) {
    return recordRepository.save(
        Record.builder()
            .mood(0)
            .sleep(8)
            .telegramId(update.getChatId())
            .build());
  }
}

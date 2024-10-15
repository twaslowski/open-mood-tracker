package de.twaslowski.moodtracker.adapter.telegram.temprecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemporaryRecordService {

  private final TemporaryRecordRepository temporaryRecordRepository;

  public TemporaryRecord createTemporaryRecordForUser(long chatId) {
    return temporaryRecordRepository.save(new TemporaryRecord(chatId));
  }

  public boolean userHasTemporaryRecord(long chatId) {
    return temporaryRecordRepository.existsById(chatId);
  }
}

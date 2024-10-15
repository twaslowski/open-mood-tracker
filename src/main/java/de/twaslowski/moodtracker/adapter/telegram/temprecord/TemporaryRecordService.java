package de.twaslowski.moodtracker.adapter.telegram.temprecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemporaryRecordService {

  private final TemporaryRecordRepository temporaryRecordRepository;

  public TemporaryRecord getTemporaryRecord(long chatId) {
    var temporaryRecord = temporaryRecordRepository.findById(chatId);
    if (temporaryRecord.isEmpty()) {
      log.info("No temporary record found for chatId: {}. Creating one.", chatId);
      return temporaryRecordRepository.save(new TemporaryRecord(chatId));
    }
    return temporaryRecord.get();
  }
}

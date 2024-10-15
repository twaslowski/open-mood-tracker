package de.twaslowski.moodtracker.adapter.telegram.handler;

import static java.lang.String.format;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.temprecord.TemporaryRecordService;
import de.twaslowski.moodtracker.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordHandler implements UpdateHandler {

  public static final String COMMAND = "/record";

  private final RecordService recordService;
  private final TemporaryRecordService temporaryRecordService;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var temporaryRecord = temporaryRecordService.getTemporaryRecord(update.chatId());
    return TelegramResponse.builder()
        .chatId(update.chatId())
        .message(format("Temporary record %d successfully created!", temporaryRecord.getTelegramId()))
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return COMMAND.equals(update.text());
  }
}

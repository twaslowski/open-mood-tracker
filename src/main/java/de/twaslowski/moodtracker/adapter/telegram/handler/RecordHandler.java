package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
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
    if (temporaryRecordService.userHasTemporaryRecord(update.chatId())) {
      return TelegramInlineKeyboardResponse.builder()
          .chatId(update.chatId())
          .message("Temporary record exists. Let's populate it # todo")
          .build();
    }
    var temporaryRecord = temporaryRecordService.createTemporaryRecordForUser(update.chatId());
    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.chatId())
        .message("Temporary record created. First metric ...")
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return COMMAND.equals(update.text());
  }
}

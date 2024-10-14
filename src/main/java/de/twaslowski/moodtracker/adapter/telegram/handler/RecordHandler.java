package de.twaslowski.moodtracker.adapter.telegram.handler;

import static java.lang.String.format;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordHandler implements UpdateHandler {

  public static final String COMMAND = "/record";

  private final RecordService recordService;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var record = recordService.createFrom(update);
    return TelegramResponse.builder()
        .chatId(update.chatId())
        .message(format("Record %d successfully created!", record.getId()))
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return COMMAND.equals(update.text());
  }
}

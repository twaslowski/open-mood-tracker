package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.CallbackGenerator;
import de.twaslowski.moodtracker.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordHandler implements UpdateHandler {

  private static final String COMMAND = "/record";

  private final RecordService recordService;
  private final CallbackGenerator callbackGenerator;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    log.info("{}: Handling record command.", update.getChatId());
    // todo there is currently nothing stopping a user from creating multiple records
    // simply add a check for existing records when /record is sent
    var record = recordService.initializeFrom(update);
    var firstMetric = recordService.getNextIncompleteMetric(record)
        .orElseThrow(() -> new IllegalStateException("No empty metrics found for record after initialization."));

    log.info("Created new record {}", record.getId());
    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .content(callbackGenerator.createCallbacks(firstMetric))
        .text(firstMetric.getChatPrompt())
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return COMMAND.equals(update.getText());
  }
}

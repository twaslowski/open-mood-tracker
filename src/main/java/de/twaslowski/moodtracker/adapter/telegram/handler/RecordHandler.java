package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.CallbackGenerator;
import de.twaslowski.moodtracker.entity.Record;
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
    var existingRecord = recordService.findIncompleteRecordForTelegramChat(update.getChatId());

    return existingRecord.map(record -> handleExistingIncompleteRecord(update, record))
        .orElseGet(() -> createNewRecord(update));
  }

  private TelegramInlineKeyboardResponse createNewRecord(TelegramUpdate update) {
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

  private TelegramInlineKeyboardResponse handleExistingIncompleteRecord(TelegramUpdate update, Record record) {
    log.info("Found existing record for chatId {}: {}", update.getChatId(), record.getId());

    // Since we know there is an incomplete record, we can safely call get()
    // noinspection OptionalGetWithoutIsPresent
    var nextMetric = recordService.getNextIncompleteMetric(record).get();

    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .content(callbackGenerator.createCallbacks(nextMetric))
        .text("You're already recording. Please complete the current metric.\n\n" + nextMetric.getChatPrompt())
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return COMMAND.equals(update.getText());
  }
}

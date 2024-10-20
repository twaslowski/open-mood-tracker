package de.twaslowski.moodtracker.adapter.telegram.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.CallbackGenerator;
import de.twaslowski.moodtracker.entity.Record;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InlineKeyboardUpdateHandler implements UpdateHandler {

  private final ObjectMapper objectMapper;
  private final RecordService recordService;
  private final CallbackGenerator callbackGenerator;

  @Override
  @SneakyThrows
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var inlineKeyboardUpdate = (TelegramInlineKeyboardUpdate) update;
    log.info("Received inline keyboard update with callback: {}", inlineKeyboardUpdate.getCallbackData());

    var existingRecord = recordService.findIncompleteRecordForTelegramChat(update.getChatId());
    return existingRecord
        .map(record -> enrichExistingRecord(record, inlineKeyboardUpdate))
        .orElseGet(() -> noRecordInProgressResponse(update));
  }

  @SneakyThrows
  private TelegramResponse enrichExistingRecord(Record existingRecord, TelegramInlineKeyboardUpdate update) {
    var receivedMetric = objectMapper.readValue(update.getCallbackData(), Metric.class);
    existingRecord.updateMetric(receivedMetric);
    recordService.store(existingRecord);

    var nextMetric = existingRecord.getFirstIncompleteMetric();
    return nextMetric
        .map(metric -> sendNextMetric(update, metric))
        .orElseGet(() -> completeRecord(update));
  }

  private TelegramResponse completeRecord(TelegramUpdate update) {
    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .message("Record saved.")
        .build();
  }

  private TelegramResponse sendNextMetric(TelegramUpdate update, Metric nextMetric) {
    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .content(callbackGenerator.createCallbacks(nextMetric))
        .message("Next metric ...")
        .build();
  }

  private TelegramResponse noRecordInProgressResponse(TelegramUpdate update) {
    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .message("You are not recording right now.")
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.hasCallback();
  }
}

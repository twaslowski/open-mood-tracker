package de.twaslowski.moodtracker.adapter.telegram.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.repository.RecordRepository;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.util.MapTransformer;
import java.util.Map;
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
  private final RecordRepository recordRepository;

  @Override
  @SneakyThrows
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var inlineKeyboardUpdate = (TelegramInlineKeyboardUpdate) update;
    var callbackData = inlineKeyboardUpdate.getCallbackData();
    log.info("Received callback data {}", callbackData);
    var metric = objectMapper.readValue(callbackData, Metric.class);

    var temporaryRecord = recordService.findIncompleteRecordForTelegramChat(update.getChatId());
    if (temporaryRecord.isPresent()) {
      var record = temporaryRecord.get();
      record.updateMetric(metric);
      recordService.store(record);
      var nextMetric = record.getFirstIncompleteMetric();
      if (nextMetric.isPresent()) {
        return TelegramInlineKeyboardResponse.builder()
            .chatId(update.getChatId())
            .content(createCallbacks(nextMetric.get()))
            .message("Next metric ...")
            .build();
      } else {
        recordService.store(record);
        return TelegramTextResponse.builder()
            .chatId(update.getChatId())
            .message("Record saved.")
            .build();
      }
    } else {
      return TelegramTextResponse.builder()
          .chatId(update.getChatId())
          .message("You are not recording right now.")
          .build();
    }
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.hasCallback();
  }

  // todo fix duplication
  private Map<String, String> createCallbacks(Metric metric) {
    return MapTransformer.transformValues(metric.getTags(), this::safeWriteValueAsString);
  }

  @SneakyThrows
  private String safeWriteValueAsString(Metric metric) {
    return objectMapper.writeValueAsString(metric);
  }
}

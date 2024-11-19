package de.twaslowski.moodtracker.adapter.telegram.handler.inlinekeyboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.UpdateHandler;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.MetricCallbackGenerator;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.Record;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.domain.entity.User.State;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricDatapointUpdateHandler implements UpdateHandler {

  private final ObjectMapper objectMapper;
  private final RecordService recordService;
  private final UserService userService;
  private final MetricCallbackGenerator metricCallbackGenerator;
  private final MessageUtil messageUtil;

  @Override
  @SneakyThrows
  @Transactional
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var inlineKeyboardUpdate = (TelegramInlineKeyboardUpdate) update;
    log.info("Received inline keyboard update with callback: {}", inlineKeyboardUpdate.getCallbackData());

    var user = userService.findByTelegramId(update.getChatId());
    var existingRecord = recordService.findIncompleteRecordsForUser(user.getId());
    return existingRecord
        .map(record -> enrichExistingRecord(record, inlineKeyboardUpdate, user))
        .orElseGet(() -> noRecordInProgressResponse(inlineKeyboardUpdate));
  }

  @SneakyThrows
  private TelegramResponse enrichExistingRecord(Record record, TelegramInlineKeyboardUpdate update, User user) {
    var receivedMetric = objectMapper.readValue(update.getCallbackData(), MetricDatapoint.class);
    record.updateMetric(receivedMetric);

    log.info("Updated record {} with metric {}, value {}",
        record.getId(), receivedMetric.metricId(), receivedMetric.value());
    return recordService.getNextIncompleteMetric(record)
        .map(nextMetric -> sendNextMetric(update, nextMetric))
        .orElseGet(() -> completeRecord(update, record, user));
  }

  private TelegramResponse completeRecord(TelegramInlineKeyboardUpdate update, Record record, User user) {
    log.info("Completing record for user with chatId {}", update.getChatId());
    userService.transitionUserState(user, State.IDLE);
    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .isTerminalAction(true)
        .answerCallbackQueryId(update.getCallbackQueryId())
        .text(messageUtil.getMessage("command.record.saved", recordService.stringifyRecord(record)))
        .build();
  }

  private TelegramResponse sendNextMetric(TelegramInlineKeyboardUpdate update, Metric nextMetric) {
    log.info("Sending next metric [id={} name={}] for incomplete record", nextMetric.getId(), nextMetric.getName());
    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .answerCallbackQueryId(update.getCallbackQueryId())
        .content(metricCallbackGenerator.createCallbacks(nextMetric))
        .text(nextMetric.getDescription())
        .build();
  }

  private TelegramResponse noRecordInProgressResponse(TelegramInlineKeyboardUpdate update) {
    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .answerCallbackQueryId(update.getCallbackQueryId())
        .text(messageUtil.getMessage("command.record.not-recording"))
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    if (update.hasCallback()) {
      var callbackData = ((TelegramInlineKeyboardUpdate) update).getCallbackData();
      return callbackData != null && callbackIsParsable(callbackData);
    } else {
      return false;
    }
  }

  private boolean callbackIsParsable(String callback) {
    try {
      objectMapper.readValue(callback, MetricDatapoint.class);
      return true;
    } catch (JsonProcessingException e) {
      return false;
    }
  }
}

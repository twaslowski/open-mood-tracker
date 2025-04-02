package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import static java.lang.String.format;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.MetricCallbackGenerator;
import de.twaslowski.moodtracker.domain.entity.Record;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RecordHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/record";

  private final RecordService recordService;
  private final UserService userService;
  private final MetricCallbackGenerator metricCallbackGenerator;

  public RecordHandler(MessageUtil messageUtil,
                       RecordService recordService,
                       UserService userService,
                       MetricCallbackGenerator metricCallbackGenerator) {
    super(messageUtil);
    this.userService = userService;
    this.recordService = recordService;
    this.metricCallbackGenerator = metricCallbackGenerator;
  }

  @Override
  @Transactional
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());
    var existingRecord = recordService.findIncompleteRecordsForUser(user.getId());

    return existingRecord.map(record -> handleExistingIncompleteRecord(update, record))
        .orElseGet(() -> createNewRecord(update));
  }

  private TelegramInlineKeyboardResponse createNewRecord(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());

    var record = recordService.initializeFrom(user);

    var firstMetric = recordService.getNextIncompleteMetric(record)
        .orElseThrow(() -> new IllegalStateException("No empty defaults found for record after initialization."));

    log.info("Created new record {} for user {}", record.getId(), user.getId());
    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .callbacks(metricCallbackGenerator.createCallbacks(firstMetric))
        .text(firstMetric.getDescription())
        .build();
  }

  private TelegramInlineKeyboardResponse handleExistingIncompleteRecord(TelegramUpdate update, Record record) {
    log.info("Found existing record for chatId {}: {}", update.getChatId(), record.getId());

    // Since we know there is an incomplete record, we can safely call get()
    // noinspection OptionalGetWithoutIsPresent
    var nextMetric = recordService.getNextIncompleteMetric(record).get();

    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .callbacks(metricCallbackGenerator.createCallbacks(nextMetric))
        .text(format("%s%n%n%s",
            messageUtil.getMessage("command.record.already-recording"),
            nextMetric.getDescription()
        ))
        .build();
  }

  @Override
  public String getCommand() {
    return COMMAND;
  }
}

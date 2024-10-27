package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import static java.lang.String.format;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.CallbackGenerator;
import de.twaslowski.moodtracker.entity.Record;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecordHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/record";

  private final RecordService recordService;
  private final UserService userService;
  private final CallbackGenerator callbackGenerator;

  public RecordHandler(MessageUtil messageUtil,
                       RecordService recordService,
                       UserService userService,
                       CallbackGenerator callbackGenerator) {
    super(COMMAND, messageUtil);
    this.userService = userService;
    this.recordService = recordService;
    this.callbackGenerator = callbackGenerator;
  }

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    log.info("Handling command");
    var user = userService.findByTelegramId(update.getChatId());
    var existingRecord = recordService.findIncompleteRecordsForUser(user.getId());

    return existingRecord.map(record -> handleExistingIncompleteRecord(update, record))
        .orElseGet(() -> createNewRecord(update));
  }

  private TelegramInlineKeyboardResponse createNewRecord(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());
    var record = recordService.initializeFrom(user);

    var firstMetric = recordService.getNextIncompleteMetric(record)
        .orElseThrow(() -> new IllegalStateException("No empty metrics found for record after initialization."));

    log.info("Created new record {}", record.getId());
    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .content(callbackGenerator.createCallbacks(firstMetric))
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
        .content(callbackGenerator.createCallbacks(nextMetric))
        .text(format("%s%n%n%s",
            messageUtil.getMessage("command.record.already-recording"),
            nextMetric.getDescription()
        ))
        .build();
  }
}
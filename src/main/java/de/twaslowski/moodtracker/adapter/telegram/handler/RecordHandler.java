package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordHandler implements UpdateHandler {

  public static final String COMMAND = "/record";

  private final RecordService recordService;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    log.info("{}: Handling record command.", update.getChatId());
    var temporaryRecord = recordService.initializeFrom(update);
    var metric = temporaryRecord.getFirstIncompleteMetric().orElseThrow(
        () -> new IllegalStateException("No incomplete metric found in freshly created record.")
    );
    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .content(metric.createCallback())
        .message("Temporary record created. First metric ...")
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return COMMAND.equals(update.getText());
  }
}

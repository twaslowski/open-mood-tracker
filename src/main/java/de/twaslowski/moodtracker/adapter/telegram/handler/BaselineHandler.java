package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class BaselineHandler implements UpdateHandler {

  private static final String COMMAND = "/baseline";

  private final UserService userService;
  private final RecordService recordService;
  private final MessageUtil messageUtil;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());
    var baselineConfiguration = user.getBaselineConfiguration();

    if (baselineConfiguration == null || baselineConfiguration.isEmpty()) {
      return noBaselineConfigurationFound(update.getChatId());
    } else {
      recordService.fromBaselineConfiguration(user);
      log.info("Baseline record created");
      return baselineRecordCreated(update.getChatId());
    }
  }

  private TelegramResponse noBaselineConfigurationFound(long chatId) {
    return TelegramTextResponse.builder()
        .chatId(chatId)
        .text(messageUtil.getMessage("command.baseline.no-configuration-found"))
        .build();
  }

  private TelegramResponse baselineRecordCreated(long chatId) {
    return TelegramTextResponse.builder()
        .chatId(chatId)
        .text(messageUtil.getMessage("command.baseline.created"))
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.getText() != null && update.getText().equals(COMMAND);
  }
}

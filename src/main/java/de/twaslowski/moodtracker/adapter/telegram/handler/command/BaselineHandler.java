package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaselineHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/baseline";

  private final UserService userService;
  private final RecordService recordService;

  public BaselineHandler(MessageUtil messageUtil,
                         UserService userService,
                         RecordService recordService) {
    super(messageUtil);
    this.userService = userService;
    this.recordService = recordService;
  }

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());

    var baselineConfiguration = userService.getBaselineConfiguration(user.getId());

    if (baselineConfiguration == null || baselineConfiguration.isEmpty()) {
      return noBaselineConfigurationFound(update.getChatId());
    } else {
      recordService.recordFromBaseline(user);
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

  @Override
  public String getCommand() {
    return COMMAND;
  }
}

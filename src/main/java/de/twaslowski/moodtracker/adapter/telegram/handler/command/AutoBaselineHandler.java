package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AutoBaselineHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/autobaseline";

  private final UserService userService;

  public AutoBaselineHandler(MessageUtil messageUtil, UserService userService) {
    super(messageUtil);
    this.userService = userService;
  }

  @Override
  @Transactional
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());

    var baselineConfiguration = userService.toggleAutoBaseline(user.getId());
    var status = baselineConfiguration ? "enabled" : "disabled";

    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text(messageUtil.getMessage("command.auto-baseline.status", status))
        .build();
  }

  @Override
  public String getCommand() {
    return COMMAND;
  }
}

package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AutoBaselineHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/auto-baseline";

  private final UserService userService;

  public AutoBaselineHandler(MessageUtil messageUtil, UserService userService) {
    super(COMMAND, messageUtil);
    this.userService = userService;
  }

  @Override
  @Transactional
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    log.info("Handling command");
    var user = userService.findByTelegramId(update.getChatId());
    var baselineConfiguration = user.toggleAutoBaseline();
    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text(messageUtil.getMessage("command.auto-baseline." + (baselineConfiguration ? "enabled" : "disabled")))
        .build();
  }
}

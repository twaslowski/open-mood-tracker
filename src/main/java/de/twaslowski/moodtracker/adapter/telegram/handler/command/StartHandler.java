package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StartHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/start";

  private final UserService userService;

  public StartHandler(UserService userService, MessageUtil messageUtil) {
    super(messageUtil);
    this.userService = userService;
  }

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var userCreated = userService.createUserFromTelegramId(update.getChatId());
    if (userCreated) {
      log.info("User created from chatId {}", update.getChatId());
    }

    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text(userCreated
            ? messageUtil.getMessage("command.start.created")
            : messageUtil.getMessage("command.start.exists"))
        .build();
  }

  @Override
  public String getCommand() {
    return COMMAND;
  }
}

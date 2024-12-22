package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class CancelHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/cancel";
  private final UserService userService;

  public CancelHandler(MessageUtil messageUtil, UserService userService) {
    super(messageUtil);
    this.userService = userService;
  }

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    userService.resetUserState(update.getChatId());
    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text(messageUtil.getMessage("command.canceled"))
        .build();
  }

  @Override
  public String getCommand() {
    return COMMAND;
  }
}

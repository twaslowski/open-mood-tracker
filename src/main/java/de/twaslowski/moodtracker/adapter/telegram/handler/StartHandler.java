package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartHandler implements UpdateHandler {

  public static final String COMMAND = "/start";

  private final UserService userService;
  private final MessageUtil messageUtil;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    log.info("Handling command");

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
  public boolean canHandle(TelegramUpdate update) {
    return update.getText() != null && COMMAND.equals(update.getText());
  }
}

package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartHandler implements UpdateHandler {

  public static final String COMMAND = "/start";
  public static final String CREATED_RESPONSE = "Hi! Nice to have you. You can record your mood now – just type /record. For help, type /help.";
  public static final String EXISTS_RESPONSE = "Hey! I think I already know you :)";

  private final UserService userService;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    log.info("{}: Handling start command.", update.getChatId());

    var userCreated = userService.createUserFromTelegramId(update.getChatId());

    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text(userCreated ? CREATED_RESPONSE : EXISTS_RESPONSE)
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.getText() != null && COMMAND.equals(update.getText());
  }
}

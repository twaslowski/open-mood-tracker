package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartHandler {

  public static final String COMMAND = "/start";
  public static final String CREATED_RESPONSE = "Hi! Nice to have you. You can record your mood now – just type /record. For help, type /help.";
  public static final String EXISTS_RESPONSE = "Hey! I think I already know you :)";

  private final UserService userService;

  public TelegramResponse handleUpdate(TelegramUpdate update) {
    log.info("Handling start command for chat {}", update.chatId());

    var userCreated = userService.createUserFromTelegramId(update.chatId());

    return TelegramResponse.builder()
        .chatId(update.chatId())
        .message(userCreated ? CREATED_RESPONSE : EXISTS_RESPONSE)
        .build();
  }
}

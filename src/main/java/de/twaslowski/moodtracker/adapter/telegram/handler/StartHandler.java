package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartHandler {

  public static final String COMMAND = "/start";
  public static final String RESPONSE = "Hello! I am MoodTracker. How are you feeling today?";

  public TelegramResponse handleUpdate(TelegramUpdate update) {
    log.info("Handling start command for chat {}", update.chatId());
    return TelegramResponse.builder()
        .chatId(update.chatId())
        .message(RESPONSE)
        .build();
  }
}

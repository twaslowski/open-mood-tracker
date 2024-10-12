package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UnknownUpdateHandler {

  public static final String RESPONSE = "Sorry, I don't understand that command. Please try again.";

  public TelegramResponse handleUpdate(TelegramUpdate update) {
    log.warn("No handler found for update {} with text: '{}'", update.updateId(), update.text());
    return TelegramResponse.builder()
        .chatId(update.chatId())
        .message(RESPONSE)
        .build();
  }
}

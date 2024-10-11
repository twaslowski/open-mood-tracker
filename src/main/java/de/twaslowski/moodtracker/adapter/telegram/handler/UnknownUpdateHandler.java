package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
public class UnknownUpdateHandler extends AbstractTelegramUpdateHandler {

  private static final String RESPONSE = "Sorry, I don't understand that command. Please try again.";

  public UnknownUpdateHandler(TelegramClient telegramClient) {
    super(telegramClient);
  }

  @Override
  public void handleUpdate(TelegramUpdate update) {
    log.warn("No handler found for update {} with text: '{}'", update.updateId(), update.text());
    respond(update, RESPONSE);
  }
}

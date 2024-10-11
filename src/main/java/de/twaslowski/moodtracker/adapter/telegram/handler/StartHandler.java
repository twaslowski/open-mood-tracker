package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
public class StartHandler extends AbstractTelegramUpdateHandler {

  public static final String COMMAND = "/start";
  private static final String RESPONSE = "Hello! I am MoodTracker. How are you feeling today?";

  public StartHandler(TelegramClient telegramClient) {
    super(telegramClient);
  }

  @Override
  public void handleUpdate(TelegramUpdate update) {
    log.info("Handling start command for chat {}", update.chatId());
    respond(update, RESPONSE);
  }
}

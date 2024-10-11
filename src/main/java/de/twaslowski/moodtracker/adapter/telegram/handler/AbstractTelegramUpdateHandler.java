package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractTelegramUpdateHandler {

  protected final TelegramClient telegramClient;

  public abstract void handleUpdate(TelegramUpdate update);

  protected void respond(TelegramUpdate update, String message) {
    var response = SendMessage.builder()
        .chatId(update.chatId())
        .text(message)
        .build();
    try {
      telegramClient.execute(response);
    } catch (Exception e) {
      handleTelegramException(e);
    }
  }

  protected void handleTelegramException(Exception e) {
    log.error("Error while sending message: {}", e.getMessage());
    throw new RuntimeException(e);
  }
}

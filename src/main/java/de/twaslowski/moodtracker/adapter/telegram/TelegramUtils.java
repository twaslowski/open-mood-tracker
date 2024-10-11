package de.twaslowski.moodtracker.adapter.telegram;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class TelegramUtils {

  public static TelegramUpdate extractUpdate(Update update) {
    var text = update.getMessage().hasText() ? update.getMessage().getText() : "";

    return TelegramUpdate.builder()
        .chatId(update.getMessage().getChatId())
        .text(text)
        .updateId(update.getUpdateId())
        .build();
  }
}

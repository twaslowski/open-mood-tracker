package de.twaslowski.moodtracker.adapter.telegram;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.exception.RequiredDataMissingException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class TelegramUtils {

  public static TelegramUpdate extractUpdate(Update update) {
    try {
      var text = update.getMessage().hasText() ? update.getMessage().getText() : "";

      return TelegramUpdate.builder()
          .chatId(update.getMessage().getChatId())
          .text(text)
          .updateId(update.getUpdateId())
          .build();
    } catch (NullPointerException e) {
      log.error("Required data missing", e);
      throw new RequiredDataMissingException(e);
    }
  }
}

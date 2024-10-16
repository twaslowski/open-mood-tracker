package de.twaslowski.moodtracker.adapter.telegram;

import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdateFactory;
import de.twaslowski.moodtracker.adapter.telegram.exception.RequiredDataMissingException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class TelegramUtils {

  public static TelegramUpdate extractUpdate(Update update) {
    try {
      return TelegramUpdateFactory.createTelegramUpdate(update);
    } catch (NullPointerException e) {
      log.error("Required data missing", e);
      throw new RequiredDataMissingException(e);
    }
  }
}

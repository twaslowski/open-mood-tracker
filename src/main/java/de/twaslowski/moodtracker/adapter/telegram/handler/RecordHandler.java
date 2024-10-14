package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import org.springframework.stereotype.Component;

@Component
public class RecordHandler implements UpdateHandler {

  public static final String COMMAND = "/record";

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    return null;
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return COMMAND.equals(update.text());
  }
}

package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;

public interface UpdateHandler {

  TelegramResponse handleUpdate(TelegramUpdate update);

  boolean canHandle(TelegramUpdate update);
}

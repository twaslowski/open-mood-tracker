package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;

public interface UpdateHandler {

  TelegramResponse handleUpdate(TelegramUpdate update);

  boolean canHandle(TelegramUpdate update);
}

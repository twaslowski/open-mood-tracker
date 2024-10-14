package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;

public interface UpdateHandler {

  TelegramResponse handleUpdate(TelegramUpdate update);

  boolean canHandle(TelegramUpdate update);
}

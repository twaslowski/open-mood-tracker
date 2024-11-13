package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;

public interface CallbackUpdateHandler<T> extends UpdateHandler {

  boolean canHandle(TelegramUpdate update);

  TelegramResponse handleUpdate(TelegramInlineKeyboardUpdate update, T data);

}

package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;

public interface CallbackUpdateHandler<T> extends UpdateHandler {

  boolean canHandle(TelegramUpdate update);

  TelegramResponse handleUpdate(TelegramInlineKeyboardUpdate update, T data);

}

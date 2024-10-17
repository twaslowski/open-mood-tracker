package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InlineKeyboardUpdateHandler implements UpdateHandler {

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var inlineKeyboardUpdate = (TelegramInlineKeyboardUpdate) update;
    log.info(inlineKeyboardUpdate.getCallbackData());
    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .message("Received callback data: " + inlineKeyboardUpdate.getCallbackData())
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.hasCallback();
  }
}

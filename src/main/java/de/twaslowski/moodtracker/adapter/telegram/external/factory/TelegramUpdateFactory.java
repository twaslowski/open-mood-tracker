package de.twaslowski.moodtracker.adapter.telegram.external.factory;

import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class TelegramUpdateFactory {

  public static TelegramUpdate createTelegramUpdate(Update update) {
    if (update.hasCallbackQuery()) {
      return TelegramInlineKeyboardUpdate.builder()
          .chatId(update.getCallbackQuery().getMessage().getChatId())
          .callbackData(update.getCallbackQuery().getData())
          .callbackQueryId(update.getCallbackQuery().getId())
          .updateId(update.getUpdateId())
          .build();
    } else {
      return TelegramTextUpdate.builder()
          .updateId(update.getUpdateId())
          .text(extractText(update))
          .chatId(extractChatId(update))
          .build();
    }
  }

  private static String extractText(Update update) {
    var message = Optional.ofNullable(update.getMessage());
    return message.map(Message::getText).orElse("");
  }

  private static long extractChatId(Update update) {
    if (update.hasCallbackQuery()) {
      return update.getCallbackQuery().getMessage().getChatId();
    } else {
      return update.getMessage().getChatId();
    }
  }
}

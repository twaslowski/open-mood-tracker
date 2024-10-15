package de.twaslowski.moodtracker.adapter.telegram.external;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

public class BotApiMessageFactory {

  public static SendMessage createBotApiMessage(TelegramResponse response) {
    return switch (response.getResponseType()) {
      case TEXT -> createTextResponse((TelegramTextResponse) response);
      case INLINE_KEYBOARD ->
          createInlineKeyboardResponse((TelegramInlineKeyboardResponse) response);
    };
  }

  private static SendMessage createTextResponse(TelegramTextResponse response) {
    return SendMessage.builder()
        .chatId(response.getChatId())
        .text(response.getMessage())
        .build();
  }

  private static SendMessage createInlineKeyboardResponse(TelegramInlineKeyboardResponse response) {
    var inlineKeyboardButtons = response.getContent().entrySet().stream()
        .map(entry -> InlineKeyboardButton.builder()
            .text(entry.getKey())
            .callbackData(entry.getValue())
            .build())
        .toList();

    return SendMessage.builder()
        .replyMarkup(InlineKeyboardMarkup.builder()
            .keyboardRow(new InlineKeyboardRow(inlineKeyboardButtons))
            .build())
        .build();
  }
}

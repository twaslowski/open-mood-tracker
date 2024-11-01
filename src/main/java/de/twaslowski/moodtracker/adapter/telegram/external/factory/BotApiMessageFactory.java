package de.twaslowski.moodtracker.adapter.telegram.external.factory;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import java.util.ArrayList;
import java.util.Map.Entry;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

public class BotApiMessageFactory {

  public static AnswerCallbackQuery createCallbackQueryAnswerResponse(TelegramResponse telegramResponse) {
    return AnswerCallbackQuery.builder()
        .text("Callback successfully processed")
        .callbackQueryId(telegramResponse.getAnswerCallbackQueryId())
        .showAlert(false)
        .build();
  }

  public static SendMessage createTextResponse(TelegramTextResponse response) {
    return SendMessage.builder()
        .chatId(response.getChatId())
        .text(response.getText())
        .build();
  }

  public static SendMessage createInlineKeyboardResponse(TelegramInlineKeyboardResponse response) {
    var rows = generateInlineKeyboardRows(response);

    return SendMessage.builder()
        .chatId(response.getChatId())
        .text(response.getText())
        .replyMarkup(InlineKeyboardMarkup.builder()
            .keyboard(rows)
            .build())
        .build();
  }

  private static ArrayList<InlineKeyboardRow> generateInlineKeyboardRows(TelegramInlineKeyboardResponse response) {
    // Generates a list of InlineKeyboardButtons, retaining order of the response's LinkedHashMap
    var inlineKeyboardButtons = new ArrayList<InlineKeyboardRow>();
    for (Entry<String, String> entry : response.getContent().entrySet()) {
      InlineKeyboardRow keyboardButtons = new InlineKeyboardRow(InlineKeyboardButton.builder()
          .text(entry.getKey())
          .callbackData(entry.getValue())
          .build());
      inlineKeyboardButtons.add(keyboardButtons);
    }
    return inlineKeyboardButtons;
  }
}

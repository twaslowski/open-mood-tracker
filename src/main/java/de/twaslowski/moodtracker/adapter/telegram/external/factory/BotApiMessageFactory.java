package de.twaslowski.moodtracker.adapter.telegram.external.factory;

import de.twaslowski.moodtracker.adapter.telegram.domain.callback.Callback;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.editable.EditableMarkupMessage;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

public class BotApiMessageFactory {

  public static SendMessage createTextResponse(TelegramTextResponse response) {
    return SendMessage.builder()
        .chatId(response.getChatId())
        .parseMode("html")
        .text(response.getText())
        .build();
  }

  public static EditMessageReplyMarkup createEditMessageReplyMarkupResponse(TelegramResponse response, EditableMarkupMessage message) {
    return EditMessageReplyMarkup.builder()
        .chatId(response.getChatId())
        .messageId(message.getMessageId())
        .replyMarkup(InlineKeyboardMarkup.builder()
            .keyboard(generateInlineKeyboardRows((TelegramInlineKeyboardResponse) response))
            .build())
        .build();
  }

  public static EditMessageText createEditMessageTextResponse(TelegramResponse response, EditableMarkupMessage message) {
    return EditMessageText.builder()
        .chatId(response.getChatId())
        .text(response.getText())
        .parseMode("html")
        .messageId(message.getMessageId())
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

  public static AnswerCallbackQuery createCallbackQueryAnswerResponse(TelegramResponse telegramResponse) {
    return AnswerCallbackQuery.builder()
        .text("Callback successfully processed")
        .callbackQueryId(telegramResponse.getAnswerCallbackQueryId())
        .showAlert(false)
        .build();
  }

  private static ArrayList<InlineKeyboardRow> generateInlineKeyboardRows(TelegramInlineKeyboardResponse response) {
    // Generates a list of InlineKeyboardButtons, retaining order of the response's LinkedHashMap
    return response.getCallbacks().stream()
        .map(callback -> new InlineKeyboardRow(buttonFromCallback(callback)))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private static InlineKeyboardButton buttonFromCallback(Callback callback) {
    return InlineKeyboardButton.builder()
        .text(callback.text())
        .callbackData(callback.data().toString())
        .build();
  }

  public static DeleteMessage createDeleteMessageResponse(EditableMarkupMessage message) {
    return DeleteMessage.builder()
        .messageId(message.getMessageId())
        .chatId(String.valueOf(message.getChatId()))
        .build();
  }
}

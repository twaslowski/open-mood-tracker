package de.twaslowski.moodtracker.adapter.telegram.external.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

class BotApiMessageFactoryTest {

  @Test
  void shouldCreateKeyboardRowsInCorrectOrder() {
    // given
    var callbacks = new LinkedHashMap<String, String>();
    callbacks.put("First", "first");
    callbacks.put("Second", "second");
    callbacks.put("Third", "third");

    var response = TelegramInlineKeyboardResponse.builder()
        .chatId(1L)
        .text("Message")
        .content(callbacks)
        .build();

    // when
    var result = BotApiMessageFactory.createInlineKeyboardResponse(response);

    // then
    var markup = (InlineKeyboardMarkup) result.getReplyMarkup();
    var keyboard = markup.getKeyboard();

    assertEquals(3, keyboard.size());
    assertEquals(getButton(0, keyboard).getText(), "First");
    assertEquals(getButton(1, keyboard).getText(), "Second");
    assertEquals(getButton(2, keyboard).getText(), "Third");
  }

  private InlineKeyboardButton getButton(int row, List<InlineKeyboardRow> keyboard) {
    return keyboard.get(row).getFirst();
  }
}
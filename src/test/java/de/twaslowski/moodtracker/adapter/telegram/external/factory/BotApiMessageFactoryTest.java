package de.twaslowski.moodtracker.adapter.telegram.external.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramInlineKeyboardResponse;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;

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
        .answerCallbackQueryId("someCallbackQueryId")
        .content(callbacks)
        .build();

    // when
    // todo revert this test to the original implementation
    var result = BotApiMessageFactory.createInlineKeyboardResponse(response);

  }
}
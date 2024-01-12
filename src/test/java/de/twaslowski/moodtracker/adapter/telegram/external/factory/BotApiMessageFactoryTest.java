package de.twaslowski.moodtracker.adapter.telegram.external.factory;

import static org.assertj.core.api.Assertions.assertThat;

import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.Callback;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.CallbackContainer;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

class BotApiMessageFactoryTest {

  @Test
  void shouldCreateKeyboardRowsInCorrectOrder() {
    // given
    var callbackOne = new Callback("First", "first");
    var callbackTwo = new Callback("Second", "second");
    var callbackThree = new Callback("Third", "third");

    var comparator = Comparator.comparing(Callback::getText);

    var callbacks = CallbackContainer.builder().callbacks(List.of(
            callbackOne,
            callbackTwo,
            callbackThree
        ))
        .comparator(comparator).build();

    var response = TelegramInlineKeyboardResponse.builder()
        .chatId(1L)
        .text("Message")
        .answerCallbackQueryId("someCallbackQueryId")
        .callbackContainer(callbacks)
        .build();

    var result = BotApiMessageFactory.createInlineKeyboardResponse(response);
    var inlineKeyboard = (InlineKeyboardMarkup) result.getReplyMarkup();

    assertThat(inlineKeyboard.getKeyboard()).hasSize(3);
    // alphabetically sorted
    assertThat(inlineKeyboard.getKeyboard().getFirst().getFirst().getText()).isEqualTo("First");
    assertThat(inlineKeyboard.getKeyboard().get(1).getFirst().getText()).isEqualTo("Second");
    assertThat(inlineKeyboard.getKeyboard().get(2).getFirst().getText()).isEqualTo("Third");
  }
}
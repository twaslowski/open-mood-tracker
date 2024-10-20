package de.twaslowski.moodtracker.adapter.telegram.external.factory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class TelegramUpdateFactoryTest {

  @Test
  void shouldExtractTextFromUpdate() {
    // Given an update with text and a chatId
    var update = new Update();
    var message = new Message();

    message.setText("some text");
    message.setChat(new Chat(1L, "private"));
    update.setUpdateId(1);
    update.setMessage(message);

    // When extracting the text
    var telegramUpdate = TelegramUpdateFactory.createTelegramUpdate(update);

    // Then the text should be extracted
    assertThat(telegramUpdate.getText()).isEqualTo("some text");
  }

  @Test
  void shouldReturnEmptyTextIfNotProvided() {
    // Given an update with text and a chatId
    var update = new Update();
    var message = new Message();

    message.setChat(new Chat(1L, "private"));
    update.setUpdateId(1);
    update.setMessage(message);

    // When extracting the text
    var telegramUpdate = TelegramUpdateFactory.createTelegramUpdate(update);

    // Then the text should be extracted
    assertThat(telegramUpdate.getText()).isEqualTo("");
  }
}

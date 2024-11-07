package de.twaslowski.moodtracker.adapter.telegram.external;

import org.telegram.telegrambots.meta.api.objects.chat.Chat;

public class TelegramObjectFactory {

  public static Chat.ChatBuilder chat() {
    return Chat.builder()
        .id(1L)
        .type("private");
  }
}

package de.twaslowski.moodtracker.adapter.telegram.spec;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;

public class TelegramUpdateSpec {

  public static TelegramUpdate.TelegramUpdateBuilder valid() {
    return TelegramUpdate.builder()
        .updateId(1)
        .chatId(1)
        .text("text");
  }
}

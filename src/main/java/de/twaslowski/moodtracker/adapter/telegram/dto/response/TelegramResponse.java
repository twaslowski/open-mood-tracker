package de.twaslowski.moodtracker.adapter.telegram.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class TelegramResponse {

  public long chatId;
  public String message;

  public TelegramResponse(long chatId) {
    this.chatId = chatId;
  }

  public enum ResponseType {
    TEXT,
    INLINE_KEYBOARD
  }

  public abstract ResponseType getResponseType();
}

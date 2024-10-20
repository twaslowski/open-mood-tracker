package de.twaslowski.moodtracker.adapter.telegram.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class TelegramResponse {

  public long chatId;
  public String message;

  public static final String UNKNOWN_COMMAND_RESPONSE = "Unfortunately, I cannot process that message.";
  public static final String ERROR_RESPONSE = "An error occurred. Please try again later.";

  public TelegramResponse(long chatId) {
    this.chatId = chatId;
  }

  public enum ResponseType {
    TEXT,
    INLINE_KEYBOARD
  }

  public static TelegramTextResponse.TelegramTextResponseBuilder error() {
    return TelegramTextResponse.builder().message(ERROR_RESPONSE);
  }

  public static TelegramTextResponse.TelegramTextResponseBuilder unhandleableUpdate() {
    return TelegramTextResponse.builder().message(UNKNOWN_COMMAND_RESPONSE);
  }

  public abstract ResponseType getResponseType();
}

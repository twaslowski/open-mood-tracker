package de.twaslowski.moodtracker.adapter.telegram.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Data
@SuperBuilder
public abstract class TelegramResponse {

  // todo these messages should be moved to messages.properties
  public static final String UNKNOWN_COMMAND_RESPONSE = "Unfortunately, I cannot process that message.";
  public static final String ERROR_RESPONSE = "An error occurred. Please try again later.";

  protected long chatId;
  protected String text;

  // If an action is terminal, it ends an interaction with the user.
  protected boolean isTerminalAction = false;

  protected String answerCallbackQueryId;

  public enum ResponseType {
    TEXT,
    INLINE_KEYBOARD
  }

  public TelegramResponse(long chatId) {
    this.chatId = chatId;
  }

  public static TelegramTextResponse.TelegramTextResponseBuilder<?, ?> error() {
    return TelegramTextResponse.builder().text(ERROR_RESPONSE);
  }

  public static TelegramTextResponse.TelegramTextResponseBuilder<?, ?> unhandleableUpdate() {
    return TelegramTextResponse.builder().text(UNKNOWN_COMMAND_RESPONSE);
  }

  public abstract ResponseType getResponseType();

  public boolean hasAnswerCallbackQueryId() {
    return answerCallbackQueryId != null;
  }
}

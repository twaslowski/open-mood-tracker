package de.twaslowski.moodtracker.adapter.telegram.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class TelegramResponse {

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

  public abstract ResponseType getResponseType();

  public boolean hasAnswerCallbackQueryId() {
    return answerCallbackQueryId != null;
  }
}

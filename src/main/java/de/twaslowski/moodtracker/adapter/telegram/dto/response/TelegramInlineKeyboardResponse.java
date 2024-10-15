package de.twaslowski.moodtracker.adapter.telegram.dto.response;

import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class TelegramInlineKeyboardResponse extends TelegramResponse {

  static ResponseType responseType = ResponseType.INLINE_KEYBOARD;
  Map<String, String> content;

  @Builder
  public TelegramInlineKeyboardResponse(long chatId, String message, Map<String, String> content) {
    super(chatId);
    this.message = message;
    this.content = content;
  }

  @Override
  public ResponseType getResponseType() {
    return responseType;
  }
}

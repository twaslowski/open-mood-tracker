package de.twaslowski.moodtracker.adapter.telegram.dto.response;

import java.util.LinkedHashMap;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class TelegramInlineKeyboardResponse extends TelegramResponse {

  static ResponseType responseType = ResponseType.INLINE_KEYBOARD;
  LinkedHashMap<String, String> content;

  @Builder
  public TelegramInlineKeyboardResponse(long chatId, String text, LinkedHashMap<String, String> content) {
    super(chatId);
    this.text = text;
    this.content = content;
  }

  @Override
  public ResponseType getResponseType() {
    return responseType;
  }
}

package de.twaslowski.moodtracker.adapter.telegram.dto.response;

import java.util.LinkedHashMap;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class TelegramInlineKeyboardResponse extends TelegramResponse {

  static ResponseType responseType = ResponseType.INLINE_KEYBOARD;
  LinkedHashMap<String, String> content;

  @Override
  public ResponseType getResponseType() {
    return responseType;
  }
}

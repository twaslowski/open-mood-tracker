package de.twaslowski.moodtracker.adapter.telegram.domain.response;

import de.twaslowski.moodtracker.adapter.telegram.domain.callback.Callback;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class TelegramInlineKeyboardResponse extends TelegramResponse {

  static ResponseType responseType = ResponseType.INLINE_KEYBOARD;
  LinkedHashMap<String, String> content;
  List<Callback> callbacks;

  @Override
  public ResponseType getResponseType() {
    return responseType;
  }
}

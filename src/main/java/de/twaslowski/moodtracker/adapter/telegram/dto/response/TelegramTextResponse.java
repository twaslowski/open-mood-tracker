package de.twaslowski.moodtracker.adapter.telegram.dto.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class TelegramTextResponse extends TelegramResponse {

  static ResponseType responseType = ResponseType.TEXT;
  String message;

  @Builder
  public TelegramTextResponse(long chatId, String message) {
    super(chatId);
    this.message = message;
  }

  @Override
  public ResponseType getResponseType() {
    return responseType;
  }
}

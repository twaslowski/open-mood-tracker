package de.twaslowski.moodtracker.adapter.telegram.domain.response;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder
public class TelegramTextResponse extends TelegramResponse {

  static ResponseType responseType = ResponseType.TEXT;

  @Override
  public ResponseType getResponseType() {
    return responseType;
  }
}

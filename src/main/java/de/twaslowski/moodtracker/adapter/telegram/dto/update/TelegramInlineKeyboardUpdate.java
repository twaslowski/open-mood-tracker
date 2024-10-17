package de.twaslowski.moodtracker.adapter.telegram.dto.update;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder
public class TelegramInlineKeyboardUpdate extends TelegramUpdate {

  String callbackData;

  @Override
  public boolean hasCallback() {
    return true;
  }
}

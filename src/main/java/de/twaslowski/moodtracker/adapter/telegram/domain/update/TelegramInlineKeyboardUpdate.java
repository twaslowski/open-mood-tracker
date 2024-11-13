package de.twaslowski.moodtracker.adapter.telegram.domain.update;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder
public class TelegramInlineKeyboardUpdate extends TelegramUpdate {

  String callbackData;
  String callbackQueryId;

  @Override
  public boolean hasCallback() {
    return true;
  }
}

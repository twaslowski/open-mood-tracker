package de.twaslowski.moodtracker.adapter.telegram.domain.update;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder
public class TelegramTextUpdate extends TelegramUpdate {

  @Override
  public boolean hasCallback() {
    return false;
  }
}

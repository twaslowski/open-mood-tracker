package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Callback {

  private final String text;
  private final Object data;
}

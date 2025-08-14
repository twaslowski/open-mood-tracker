package de.twaslowski.moodtracker.adapter.rest.v2.dto;

import java.time.ZonedDateTime;

public record Datapoint(
    ZonedDateTime timestamp,
    int value
) {

  public static Datapoint of(ZonedDateTime timestamp, int value) {
    return new Datapoint(timestamp, value);
  }
}

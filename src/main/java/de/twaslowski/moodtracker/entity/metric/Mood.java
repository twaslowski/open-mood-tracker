package de.twaslowski.moodtracker.entity.metric;

import lombok.AllArgsConstructor;

public class Mood extends Metric {

  public static final MetricType TYPE = MetricType.MOOD;

  @AllArgsConstructor
  public enum MoodValues implements MetricValue {
    SEVERELY_MANIC(3),
    MANIC(2),
    SLIGHTLY_MANIC(1),
    NEUTRAL(0),
    SLIGHTLY_DEPRESSED(-1),
    DEPRESSED(-2),
    SEVERELY_DEPRESSED(-3);

    private final int value;

    @Override
    public int getValue() {
      return value;
    }
  }

  public Mood() {
    super(TYPE);
  }
}

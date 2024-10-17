package de.twaslowski.moodtracker.entity.metric;

import java.util.Map;

public class Mood extends Metric {

  public static final String TYPE = "MOOD";
  private static final int LOWER_BOUND = -3;
  private static final int UPPER_BOUND = 3;

  private static final Map<String, Integer> TAGS = Map.of(
      "SEVERELY_MANIC", 3,
      "MANIC", 2,
      "HYPOMANIC", 1,
      "NEUTRAL", 0,
      "LIGHTLY DEPRESSED", -1,
      "DEPRESSED", -2,
      "SEVERELY_DEPRESSED", -3
  );

  public Mood() {
    super(TYPE, LOWER_BOUND, UPPER_BOUND, TAGS, null);
  }

  public Mood(int value) {
    super(TYPE, LOWER_BOUND, UPPER_BOUND, TAGS, value);
  }

  public static Mood empty() {
    return new Mood();
  }

  public static Mood of(int value) {
    return new Mood(value);
  }
}

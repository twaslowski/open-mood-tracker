package de.twaslowski.moodtracker.entity.metric;

import java.util.Map;

public class Mood extends Metric {

  public static final String TYPE = "MOOD";

  public Mood() {
    super(TYPE, null);
  }

  public Mood(int value) {
    super(TYPE, value);
  }

  public static final Map<Metric, String> CALLBACK_MAPPING = Map.of(
      new Mood(3), "SEVERELY_MANIC",
      new Mood(2), "MANIC",
      new Mood(1), "HYPOMANIC",
      new Mood(0), "NEUTRAL",
      new Mood(-1), "MILDLY_DEPRESSED",
      new Mood(-2), "MODERATELY_DEPRESSED",
      new Mood(-3), "SEVERELY_DEPRESSED"
  );

  public static Mood empty() {
    return new Mood();
  }

  public static Mood of(int value) {
    return new Mood(value);
  }
}

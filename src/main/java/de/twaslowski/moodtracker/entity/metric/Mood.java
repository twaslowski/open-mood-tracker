package de.twaslowski.moodtracker.entity.metric;

import java.util.Map;

public class Mood extends Metric {

  public static final String TYPE = "MOOD";
  private static final String PROMPT = "How do you feel today?";
  private static final Integer MIN_VALUE = -3;
  private static final Integer MAX_VALUE = 3;
  public static final Map<Integer, String> LABELS = Map.of(
      3, "SEVERELY_MANIC",
      2, "MANIC",
      1, "HYPOMANIC",
      0, "NEUTRAL",
      -1, "MILDLY_DEPRESSED",
      -2, "MODERATELY_DEPRESSED",
      -3, "SEVERELY_DEPRESSED"
  );

  public Mood() {
    super(TYPE, PROMPT, MIN_VALUE, MAX_VALUE, LABELS);
  }
}

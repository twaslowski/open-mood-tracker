package de.twaslowski.moodtracker.entity.metric;

import java.util.Map;

public class Mood extends Metric {

  public static final String NAME = "MOOD";
  private static final String PROMPT = "How do you feel today?";

  private static final Integer DEFAULT_OWNER = 1;
  private static final Integer MIN_VALUE = -3;
  private static final Integer MAX_VALUE = 3;
  private static final Integer DEFAULT = 0;

  private static final SortOrder SORT_ORDER = SortOrder.DESC;

  private static final Map<Integer, String> LABELS = Map.of(
      3, "SEVERELY_MANIC",
      2, "MANIC",
      1, "HYPOMANIC",
      0, "NEUTRAL",
      -1, "MILDLY_DEPRESSED",
      -2, "MODERATELY_DEPRESSED",
      -3, "SEVERELY_DEPRESSED"
  );

  public static final Metric INSTANCE = Metric.builder()
      .id(1)
      .name(NAME)
      .ownerId(DEFAULT_OWNER)
      .description(PROMPT)
      .maxValue(MAX_VALUE)
      .minValue(MIN_VALUE)
      .defaultValue(DEFAULT)
      .labels(LABELS)
      .sortOrder(SORT_ORDER)
      .build();
}

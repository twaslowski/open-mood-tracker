package de.twaslowski.moodtracker.entity.metric;

import java.util.Map;

public class Mood extends Metric {

  public static final String NAME = "Mood";
  private static final String PROMPT = "How do you feel today?";

  private static final Integer DEFAULT_OWNER = 1;
  private static final Integer MIN_VALUE = -3;
  private static final Integer MAX_VALUE = 3;
  private static final Integer DEFAULT = 0;

  private static final SortOrder SORT_ORDER = SortOrder.DESC;

  private static final Map<Integer, String> LABELS = Map.of(
      3, "Strongly Manic",
      2, "Manic",
      1, "Hypomanic",
      0, "Neutral",
      -1, "Mildly Depressed",
      -2, "Depressed",
      -3, "Strongly Depressed"
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

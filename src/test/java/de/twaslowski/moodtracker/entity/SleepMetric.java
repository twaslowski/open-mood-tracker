package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.domain.entity.Metric;

public class SleepMetric extends Metric {

  public static final String NAME = "Sleep";
  private static final String PROMPT = "How much did you sleep today?";

  private static final String DEFAULT_OWNER = "1";
  private static final int MIN_VALUE = 4;
  private static final int MAX_VALUE = 12;
  private static final int DEFAULT_VALUE = 8;
  private static final SortOrder SORT_ORDER = SortOrder.ASC;

  public static final Metric INSTANCE = Metric.builder()
      .id(2)
      .name(NAME)
      .ownerId(DEFAULT_OWNER)
      .description(PROMPT)
      .minValue(MIN_VALUE)
      .maxValue(MAX_VALUE)
      .defaultValue(DEFAULT_VALUE)
      .sortOrder(SORT_ORDER)
      .build();
}

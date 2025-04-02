package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.value.Label;
import java.util.List;

public class MoodMetric extends Metric {

  public static final String NAME = "Mood";
  private static final String PROMPT = "How do you feel today?";

  private static final String DEFAULT_OWNER = "1";
  private static final Integer MIN_VALUE = -3;
  private static final Integer MAX_VALUE = 3;
  private static final Integer DEFAULT = 0;

  private static final SortOrder SORT_ORDER = SortOrder.DESC;

  private static final List<Label> LABELS = List.of(
      Label.of(3, "Strongly Manic"),
      Label.of(2, "Manic"),
      Label.of(1, "Hypomanic"),
      Label.of(0, "Neutral"),
      Label.of(-1, "Mildly Depressed"),
      Label.of(-2, "Depressed"),
      Label.of(-3, "Strongly Depressed")
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

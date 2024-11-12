package de.twaslowski.moodtracker.config.defaults;

import de.twaslowski.moodtracker.entity.metric.Metric;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SleepMetric extends Metric {

  public static final String NAME = "Sleep";
  private static final String PROMPT = "How much did you sleep today?";

  private static final Integer DEFAULT_OWNER = 1;
  private static final int MIN_VALUE = 4;
  private static final int MAX_VALUE = 12;
  private static final int DEFAULT_VALUE = 8;
  private static final SortOrder SORT_ORDER = SortOrder.ASC;

  private static final Map<Integer, String> LABELS = IntStream.range(MIN_VALUE, MAX_VALUE + 1)
      .boxed()
      .collect(Collectors.toMap(Function.identity(), Object::toString));

  public static final Metric INSTANCE = Metric.builder()
      .id(2)
      .name(NAME)
      .ownerId(DEFAULT_OWNER)
      .description(PROMPT)
      .minValue(MIN_VALUE)
      .maxValue(MAX_VALUE)
      .defaultValue(DEFAULT_VALUE)
      .labels(LABELS)
      .sortOrder(SORT_ORDER)
      .build();
}

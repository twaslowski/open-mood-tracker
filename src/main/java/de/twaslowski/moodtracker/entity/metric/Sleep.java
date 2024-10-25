package de.twaslowski.moodtracker.entity.metric;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sleep extends Metric {

  public static final String NAME = "SLEEP";
  private static final String PROMPT = "How much did you sleep today?";

  private static final int MIN_VALUE = 4;
  private static final int MAX_VALUE = 12;
  private static final int DEFAULT_VALUE = 8;

  private static final Map<Integer, String> LABELS = IntStream.range(MIN_VALUE, MAX_VALUE + 1)
      .boxed()
      .collect(Collectors.toMap(Function.identity(), Object::toString));

  private static final Comparator<MetricDatapoint> COMPARATOR = Comparator.comparingInt(MetricDatapoint::value);

  public static final Metric INSTANCE = Metric.builder()
      .name(NAME)
      .ownerId(0)
      .description(PROMPT)
      .maxValue(MIN_VALUE)
      .minValue(MAX_VALUE)
      .defaultValue(DEFAULT_VALUE)
      .labels(LABELS)
      .build();
}

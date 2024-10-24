package de.twaslowski.moodtracker.entity.metric;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sleep extends Metric {

  public static final String TYPE = "SLEEP";
  private static final String PROMPT = "How much did you sleep today?";
  private static final int MIN_VALUE = 4;
  private static final int MAX_VALUE = 12;
  private static final Comparator<MetricDatapoint> COMPARATOR = Comparator.comparingInt(MetricDatapoint::value);

  public static Map<MetricDatapoint, String> LABELS = IntStream.range(MIN_VALUE, MAX_VALUE + 1)
      .boxed()
      .collect(Collectors.toMap(Sleep::datapoint, Object::toString));

  public Sleep() {
    super(
        TYPE,
        PROMPT,
        MIN_VALUE,
        MAX_VALUE,
        LABELS,
        COMPARATOR,
        8
    );
  }

  private static MetricDatapoint datapoint(Integer value) {
    return new MetricDatapoint(TYPE, value);
  }
}

package de.twaslowski.moodtracker.entity.metric;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sleep extends Metric {

  public static final String TYPE = "SLEEP";
  private static final int LOWER_BOUND = 4;
  private static final int UPPER_BOUND = 12;

  public static Map<Metric, String> CALLBACK_MAPPING = IntStream.range(LOWER_BOUND, UPPER_BOUND + 1)
      .boxed()
      .collect(Collectors.toMap(Sleep::of, Object::toString));

  public Sleep() {
    super(TYPE, null);
  }

  public Sleep(int value) {
    super(TYPE, value);
  }

  public static Sleep empty() {
    return new Sleep();
  }

  public static Sleep of(int value) {
    return new Sleep(value);
  }
}

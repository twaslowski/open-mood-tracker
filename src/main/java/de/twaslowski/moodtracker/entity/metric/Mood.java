package de.twaslowski.moodtracker.entity.metric;

import java.util.Comparator;
import java.util.Map;

public class Mood extends Metric {

  public static final String TYPE = "MOOD";
  private static final String PROMPT = "How do you feel today?";
  private static final Integer MIN_VALUE = -3;
  private static final Integer MAX_VALUE = 3;
  public static final Map<MetricDatapoint, String> LABELS = Map.of(
      datapoint(3), "SEVERELY_MANIC",
      datapoint(2), "MANIC",
      datapoint(1), "HYPOMANIC",
      datapoint(0), "NEUTRAL",
      datapoint(-1), "MILDLY_DEPRESSED",
      datapoint(-2), "MODERATELY_DEPRESSED",
      datapoint(-3), "SEVERELY_DEPRESSED"
  );
  private static final Comparator<MetricDatapoint> COMPARATOR = Comparator.comparingInt(MetricDatapoint::value).reversed();

  public Mood() {
    super(TYPE, PROMPT, MIN_VALUE, MAX_VALUE, LABELS, COMPARATOR);
  }

  private static MetricDatapoint datapoint(Integer value) {
    return new MetricDatapoint(TYPE, value);
  }
}

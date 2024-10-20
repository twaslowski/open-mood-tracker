package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import de.twaslowski.moodtracker.entity.metric.Metric;
import java.util.Comparator;

public class MetricComparator {

  public static Comparator<Metric> ascending() {
    return Comparator.comparing(Metric::getValue);
  }

  public static Comparator<Metric> descending() {
    return Comparator.comparing(Metric::getValue).reversed();
  }
}

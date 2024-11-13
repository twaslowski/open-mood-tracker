package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import java.util.Comparator;

public class MetricComparator {

  public static Comparator<MetricDatapoint> ascending() {
    return Comparator.comparing(MetricDatapoint::value);
  }

  public static Comparator<MetricDatapoint> descending() {
    return Comparator.comparing(MetricDatapoint::value).reversed();
  }
}

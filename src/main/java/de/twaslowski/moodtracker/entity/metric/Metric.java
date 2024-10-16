package de.twaslowski.moodtracker.entity.metric;

public abstract class Metric {

  protected final MetricType type;

  public enum MetricType {
    MOOD,
    SLEEP,
  }

  public Metric(MetricType type) {
    this.type = type;
  }

  public interface MetricValue {

    int getValue();
  }
}

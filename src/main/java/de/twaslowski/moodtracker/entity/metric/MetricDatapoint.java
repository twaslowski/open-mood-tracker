package de.twaslowski.moodtracker.entity.metric;

public record MetricDatapoint(
    String metricName,
    Integer value
) {

  public static MetricDatapoint forMetric(String metric) {
    return new MetricDatapoint(metric, null);
  }
}

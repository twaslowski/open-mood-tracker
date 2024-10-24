package de.twaslowski.moodtracker.entity.metric;

public record MetricDatapoint(
    String metricName,
    Integer value
) {

  public static MetricDatapoint forMetric(String metric) {
    return new MetricDatapoint(metric, null);
  }

  public static MetricDatapoint fromMetricDefault(Metric metric) {
    if (metric.getDefaultValue() == null) {
      throw new IllegalArgumentException("Metric " + metric.getName() + " has no default value.");
    }
    return new MetricDatapoint(metric.getName(), metric.getDefaultValue());
  }
}

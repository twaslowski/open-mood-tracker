package de.twaslowski.moodtracker.entity.metric;

public record MetricDatapoint(
    String metricName,
    Integer value
) {

  public static MetricDatapoint emptyForMetric(String metricName) {
    return new MetricDatapoint(metricName, null);
  }

  public static MetricDatapoint forMetricWithValue(Metric metric, Integer value) {
    if (value < metric.getMinValue() || value > metric.getMaxValue()) {
      throw new IllegalArgumentException("Value " + value + " is not within the allowed range for metric " + metric.getName());
    }
    return new MetricDatapoint(metric.getName(), value);
  }

  public static MetricDatapoint defaultForMetric(Metric metric) {
    if (metric.getDefaultValue() == null) {
      throw new IllegalArgumentException("Metric " + metric.getName() + " has no default value.");
    }
    return new MetricDatapoint(metric.getName(), metric.getDefaultValue());
  }
}

package de.twaslowski.moodtracker.entity.metric;

public record MetricDatapoint(
    Metric metric,
    Integer value
) {

}

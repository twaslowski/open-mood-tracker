package de.twaslowski.moodtracker.domain.value;

import lombok.Builder;

@Builder
public record MetricDatapoint(
    long metricId,
    Integer value
) {

}

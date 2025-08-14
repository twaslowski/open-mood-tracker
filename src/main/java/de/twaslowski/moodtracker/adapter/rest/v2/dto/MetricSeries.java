package de.twaslowski.moodtracker.adapter.rest.v2.dto;

import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
import de.twaslowski.moodtracker.domain.value.Label;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record MetricSeries(
    long metricId,
    String name,
    int minValue,
    int maxValue,
    List<Label> labels,
    boolean tracked,
    List<Datapoint> trackingData
) {

  public static MetricSeries from(MetricConfiguration metricConfiguration) {
    return MetricSeries.builder()
        .metricId(metricConfiguration.getMetric().getId())
        .name(metricConfiguration.getMetric().getName())
        .minValue(metricConfiguration.getMetric().getMinValue())
        .maxValue(metricConfiguration.getMetric().getMaxValue())
        .labels(metricConfiguration.getMetric().getLabels())
        .tracked(metricConfiguration.isTracked())
        .trackingData(new ArrayList<>())
        .build();
  }

  public void add(Datapoint datapoint) {
    this.trackingData.add(datapoint);
  }
}

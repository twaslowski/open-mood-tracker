package de.twaslowski.moodtracker.domain.dto;

import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
import de.twaslowski.moodtracker.domain.value.Label;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record MetricDTO(
    long id,
    String name,
    String description,
    boolean isDefault,
    int minValue,
    int maxValue,
    int baseline,
    List<Label> labels,
    boolean tracked,
    String trackedMetricId // metricConfigurationId
) {

  public static MetricDTO from(Metric metric) {
    return MetricDTO.builder()
        .id(metric.getId())
        .name(metric.getName())
        .description(metric.getDescription())
        .isDefault(metric.isDefaultMetric())
        .minValue(metric.getMinValue())
        .maxValue(metric.getMaxValue())
        .baseline(metric.getDefaultValue())
        .labels(metric.getLabels())
        .tracked(false)
        .trackedMetricId(null)
        .build();
  }

  public static MetricDTO from(MetricConfiguration metricConfiguration) {
    Metric metric = metricConfiguration.getMetric();
    return MetricDTO.builder()
        // Derived from Metric
        .id(metric.getId())
        .name(metric.getName())
        .description(metric.getDescription())
        .isDefault(metric.isDefaultMetric())
        .minValue(metric.getMinValue())
        .maxValue(metric.getMaxValue())
        .labels(metric.getLabels())
        // User Configuration
        .trackedMetricId(metricConfiguration.getId())
        .tracked(metricConfiguration.isTracked())
        .baseline(metricConfiguration.getBaselineValue())
        .build();
  }

  public boolean validateLabels() {
    Set<Integer> labelValues = labels.stream()
        .map(Label::value)
        .collect(Collectors.toSet());
    return labelValues.stream().allMatch(this::isWithinBounds)
        && labelValues.size() == (maxValue - minValue + 1);
  }

  private boolean isWithinBounds(Integer value) {
    return value <= maxValue || value >= minValue;
  }
}

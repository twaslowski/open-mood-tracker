package de.twaslowski.moodtracker.domain.dto;

import de.twaslowski.moodtracker.domain.value.Label;
import java.util.List;
import lombok.Builder;

@Builder
public record DatapointDTO(
    long metricId,
    String metricName,
    String metricDescription,
    int minValue,
    int maxValue,
    int baselineValue,
    int datapointValue,
    List<Label> labels
) {

}

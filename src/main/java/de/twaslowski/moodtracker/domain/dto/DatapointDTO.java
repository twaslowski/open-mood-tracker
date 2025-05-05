package de.twaslowski.moodtracker.domain.dto;

import de.twaslowski.moodtracker.domain.value.Label;
import lombok.Builder;
import java.util.List;

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

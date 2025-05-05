package de.twaslowski.moodtracker.domain.dto;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record RecordDTO(
    ZonedDateTime timestamp,
    List<DatapointDTO> datapoints
) {

}

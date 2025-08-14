package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.domain.entity.Record;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RecordSpec {

  public static Record.RecordBuilder forUser(String userId, List<Long> metricIds) {
    return Record.builder()
        .status(Record.Status.COMPLETED)
        .creationTimestamp(ZonedDateTime.now())
        .userId(userId)
        .values(metricIds.stream()
            .map(metricId -> new MetricDatapoint(metricId, 5))
            .collect(Collectors.toList())
        );
  }
}

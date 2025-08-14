package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.domain.entity.Record;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;

import java.time.ZonedDateTime;
import java.util.List;

public class RecordSpec {

  public static Record.RecordBuilder forUser(String userId) {
    return Record.builder()
        .status(Record.Status.COMPLETED)
        .creationTimestamp(ZonedDateTime.now())
        .userId(userId)
        .values(List.of(
            new MetricDatapoint(1L, 1),
            new MetricDatapoint(2L, 8))
        );
  }
}

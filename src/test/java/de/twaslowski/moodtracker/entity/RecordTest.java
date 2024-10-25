package de.twaslowski.moodtracker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import java.util.List;
import org.junit.jupiter.api.Test;

class RecordTest {

  @Test
  void shouldOverwriteExistingMetric() {
    // given
    var record = Record.builder()
        .values(List.of(new MetricDatapoint(Mood.NAME, 1)))
        .build();

    var newDatapoint = new MetricDatapoint(Mood.NAME, 2);

    // when
    record.updateMetric(newDatapoint);

    // then
    assertEquals(1, record.getValues().size());
    assertTrue(record.getValues().contains(newDatapoint));
  }
}
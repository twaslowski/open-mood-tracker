package de.twaslowski.moodtracker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RecordTest {

  @Test
  void shouldOverwriteExistingMetric() {
    // given
    var record = Record.builder()
        .values(new HashSet<>(Set.of(new MetricDatapoint(Mood.TYPE, 1))))
        .build();
    var metric = new MetricDatapoint(Mood.TYPE, 2);

    // when
    record.updateMetric(metric);

    // then
    assertEquals(1, record.getValues().size());
    assertTrue(record.getValues().contains(metric));
  }
}
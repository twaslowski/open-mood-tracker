package de.twaslowski.moodtracker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.twaslowski.moodtracker.entity.metric.Mood;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RecordTest {

  @Test
  void shouldOverwriteExistingMetric() {
    // given
    var record = Record.builder()
        .values(new HashSet<>(Set.of(Mood.of(1))))
        .build();
    var metric = Mood.of(2);

    // when
    record.updateMetric(metric);

    // then
    assertEquals(1, record.getValues().size());
    assertTrue(record.getValues().contains(metric));
  }
}
package de.twaslowski.moodtracker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import de.twaslowski.moodtracker.domain.entity.Record;
import org.junit.jupiter.api.Test;

class RecordTest {

  @Test
  void shouldOverwriteExistingMetric() {
    // given
    var record = Record.builder()
        .values(List.of(MoodMetric.INSTANCE.datapointWithValue(1)))
        .build();

    var newDatapoint = MoodMetric.INSTANCE.datapointWithValue(2);

    // when
    record.update(newDatapoint);

    // then
    assertEquals(1, record.getValues().size());
    assertTrue(record.getValues().contains(newDatapoint));
  }
}
package de.twaslowski.moodtracker.entity.metric;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class SleepTest {

  @Test
  void testTagsGeneration() {
    assertThat(Sleep.LABELS).isEqualTo(
        Map.of(
            Sleep.of(4), "4",
            Sleep.of(5), "5",
            Sleep.of(6), "6",
            Sleep.of(7), "7",
            Sleep.of(8), "8",
            Sleep.of(9), "9",
            Sleep.of(10), "10",
            Sleep.of(11), "11",
            Sleep.of(12), "12"
        )
    );
  }
}
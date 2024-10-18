package de.twaslowski.moodtracker.entity.metric;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class SleepTest {

  @Test
  void testTagsGeneration() {
    assertThat(new Sleep().getTags()).isEqualTo(
        Map.of(
            "4", Sleep.of(4),
            "5", Sleep.of(5),
            "6", Sleep.of(6),
            "7", Sleep.of(7),
            "8", Sleep.of(8),
            "9", Sleep.of(9),
            "10", Sleep.of(10),
            "11", Sleep.of(11),
            "12", Sleep.of(12)
        )
    );
  }
}
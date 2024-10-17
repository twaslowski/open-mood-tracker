package de.twaslowski.moodtracker.entity.metric;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class SleepTest {

  @Test
  void testTagsGeneration() {
    assertThat(new Sleep().getTags()).isEqualTo(
        Map.of(
            "4", 4,
            "5", 5,
            "6", 6,
            "7", 7,
            "8", 8,
            "9", 9,
            "10", 10,
            "11", 11,
            "12", 12
        )
    );
  }
}
package de.twaslowski.moodtracker.entity.metric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MetricTest {

  @Test
  void shouldProduceCallbacksForMood() {
    Metric mood = new Mood();
    assertEquals(7, mood.getTags().size());
    assertEquals(Mood.of(3), mood.getTags().get("SEVERELY_MANIC"));
    assertEquals(Mood.of(2), mood.getTags().get("MANIC"));
    assertEquals(Mood.of(1), mood.getTags().get("HYPOMANIC"));
    assertEquals(Mood.of(0), mood.getTags().get("NEUTRAL"));
    assertEquals(Mood.of(-1), mood.getTags().get("LIGHTLY DEPRESSED"));
    assertEquals(Mood.of(-2), mood.getTags().get("DEPRESSED"));
    assertEquals(Mood.of(-3), mood.getTags().get("SEVERELY_DEPRESSED"));
  }

  @Test
  void metricShouldBeUnequalForDifferentValues() {
    assertThat(new Mood(1)).isNotEqualTo(new Mood(2));
  }
}
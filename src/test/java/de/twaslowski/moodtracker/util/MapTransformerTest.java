package de.twaslowski.moodtracker.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class MapTransformerTest {

  @Test
  void shouldProperlyTransformValues() {
    // given
    var map = Map.of("key1", 1, "key2", 2);

    // when
    var result = MapTransformer.transformValues(map, value -> value * 2);

    // then
    assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of("key1", 2, "key2", 4));
  }
}

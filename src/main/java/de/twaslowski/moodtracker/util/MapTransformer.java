package de.twaslowski.moodtracker.util;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapTransformer {

  public static <K, V, R> Map<K, R> transformValues(Map<K, V> map, Function<V, R> transformer) {
    return map.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> transformer.apply(entry.getValue())
        ));
  }
}

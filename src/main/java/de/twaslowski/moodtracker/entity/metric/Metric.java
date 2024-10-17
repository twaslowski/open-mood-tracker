package de.twaslowski.moodtracker.entity.metric;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Map.Entry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public abstract class Metric {

  protected final String type;
  protected final int lowerBound;
  protected final int upperBound;
  protected Map<String, Integer> tags;

  protected final Integer value;

  public Map<String, String> getTagsAsStrings() {
    return tags.entrySet().stream()
        .collect(toMap(Entry::getKey, e -> e.getValue().toString()));
  }
}
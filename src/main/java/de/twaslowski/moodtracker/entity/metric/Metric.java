package de.twaslowski.moodtracker.entity.metric;

import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public abstract class Metric {

  protected final String name;
  protected final String chatPrompt;

  protected final Integer minValue;
  protected final Integer maxValue;
  protected final Map<Integer, String> labels;
}
package de.twaslowski.moodtracker.entity.metric;

import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public abstract class Metric {

  protected final String type;

  protected final Integer value;

  public abstract Map<String, Metric> getTags();
}
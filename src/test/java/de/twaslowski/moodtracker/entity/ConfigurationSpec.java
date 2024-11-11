package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.entity.metric.Mood;
import java.util.List;

public class ConfigurationSpec {

  public static Configuration.ConfigurationBuilder valid() {
    return Configuration.builder()
        .id(1)
        .baselineMetrics(List.of(Mood.INSTANCE.defaultDatapoint()))
        .autoBaselineEnabled(true);
  }
}

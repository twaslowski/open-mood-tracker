package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import java.util.List;

public class ConfigurationSpec {

  public static Configuration.ConfigurationBuilder valid() {
    return Configuration.builder()
        .id(1)
        .baselineMetrics(List.of(
            Mood.INSTANCE.defaultDatapoint(),
            Sleep.INSTANCE.defaultDatapoint()
        ))
        .trackedMetricIds(List.of(1L, 2L))
        .autoBaselineEnabled(true);
  }

  public static Configuration.ConfigurationBuilder withoutBaselineConfiguration() {
    return Configuration.builder()
        .id(1)
        .baselineMetrics(List.of())
        .autoBaselineEnabled(false);
  }
}

package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.entity.metric.Mood;
import java.util.Set;

public class ConfigurationSpec {

  public static Configuration.ConfigurationBuilder valid() {
    return Configuration.builder()
        .id(1)
        .baselineConfiguration(Set.of(Mood.defaultDatapoint()))
        .autoBaselineEnabled(true)
        .notificationsEnabled(true);
  }
}

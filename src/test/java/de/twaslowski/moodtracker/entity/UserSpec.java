package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.entity.metric.Mood;
import java.util.List;

public class UserSpec {

  public static User.UserBuilder valid() {
    return User.builder()
        .id(1)
        .telegramId(1)
        .configuration(ConfigurationSpec.valid()
            .baselineMetrics(List.of(
                Mood.INSTANCE.defaultDatapoint()
            ))
            .trackedMetricIds(List.of(1L, 2L))
            .build());
  }

  public static User.UserBuilder noBaselineConfiguration() {
    return User.builder()
        .telegramId(1)
        .configuration(ConfigurationSpec.valid()
            .baselineMetrics(List.of())
            .build()
        );
  }
}

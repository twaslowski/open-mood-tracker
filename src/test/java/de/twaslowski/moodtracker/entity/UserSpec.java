package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import java.util.List;

public class UserSpec {

  public static User.UserBuilder valid() {
    return User.builder()
        .id(1)
        .telegramId(1)
        .configuration(ConfigurationSpec.valid()
            .baselineMetrics(List.of(
                    MetricDatapoint.fromMetricDefault(Mood.INSTANCE)
                ))
            .metrics(List.of(Mood.NAME, Sleep.NAME))
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

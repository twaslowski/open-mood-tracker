package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import java.util.List;

public class UserSpec {

  public static User.UserBuilder valid() {
    return User.builder()
        .id(1)
        .telegramId(1)
        .configuration(ConfigurationSpec.valid()
            .baselineConfiguration(
                List.of(
                    MetricDatapoint.fromMetricDefault(Mood.INSTANCE)
                )
            ).build());
  }

  public static User.UserBuilder noBaselineConfiguration() {
    return User.builder()
        .telegramId(1)
        .configuration(ConfigurationSpec.valid().build()
        );
  }
}

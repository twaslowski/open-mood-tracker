package de.twaslowski.moodtracker.entity;

import java.util.List;

public class UserSpec {

  public static User.UserBuilder valid() {
    return User.builder()
        .id(1)
        .telegramId(1)
        .configuration(ConfigurationSpec.valid().build());
  }

  public static User.UserBuilder noBaselineConfiguration() {
    return User.builder()
        .id(1)
        .telegramId(1)
        .configuration(ConfigurationSpec.valid()
            .baselineConfiguration(List.of())
            .build()
        );
  }
}

package de.twaslowski.moodtracker.entity;

import java.util.Set;

public class UserSpec {

  public static User.UserBuilder valid() {
    return User.builder()
        .id(1)
        .telegramId(1)
        .baselineConfiguration(Set.of())
        .autoBaselineEnabled(true)
        .notificationsEnabled(true);
  }
}

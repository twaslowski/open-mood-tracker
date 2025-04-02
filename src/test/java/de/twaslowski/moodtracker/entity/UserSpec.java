package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.domain.entity.User;

public class UserSpec {

  public static User.UserBuilder valid() {
    return User.builder()
        .telegramId(1)
        .autoBaselineEnabled(false);
  }
}

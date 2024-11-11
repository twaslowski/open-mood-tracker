package de.twaslowski.moodtracker.entity;

public class UserSpec {

  public static User.UserBuilder valid() {
    return User.builder()
        .id(1)
        .telegramId(1);
  }

  public static User.UserBuilder noBaselineConfiguration() {
    return User.builder()
        .id(1)
        .telegramId(1);
  }
}

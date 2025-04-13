package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.domain.entity.User;
import java.util.UUID;

public class UserSpec {

  public static User.UserBuilder valid() {
    return User.builder()
        .id(UUID.randomUUID().toString())
        .telegramId(1)
        .autoBaselineEnabled(false);
  }
}

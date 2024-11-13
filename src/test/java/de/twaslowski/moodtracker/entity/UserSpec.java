package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.domain.entity.User.State;

public class UserSpec {

  public static User.UserBuilder valid() {
    return User.builder()
        .id(1)
        .state(State.IDLE)
        .telegramId(1);
  }
}

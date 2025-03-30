package de.twaslowski.moodtracker.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String id) {
    super(String.format("User with id %s not found", id));
  }

  public UserNotFoundException(long id) {
    super(String.format("User with chatId %d not found", id));
  }
}

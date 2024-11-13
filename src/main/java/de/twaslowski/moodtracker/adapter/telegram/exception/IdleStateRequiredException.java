package de.twaslowski.moodtracker.adapter.telegram.exception;

public class IdleStateRequiredException extends RuntimeException {

  public IdleStateRequiredException(String message) {
    super(message);
  }
}

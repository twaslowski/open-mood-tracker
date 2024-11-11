package de.twaslowski.moodtracker.exception;

public class ConfigurationNotFoundException extends RuntimeException {

  public ConfigurationNotFoundException(long userId) {
    super("No configuration found for user with id " + userId);
  }

}

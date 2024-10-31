package de.twaslowski.moodtracker.adapter.telegram.exception;

public class NoTelegramTokenProvidedException extends RuntimeException {

  public NoTelegramTokenProvidedException() {
    super("No (or empty) Telegram token provided.");
  }

}

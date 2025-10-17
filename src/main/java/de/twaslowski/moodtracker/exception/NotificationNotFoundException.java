package de.twaslowski.moodtracker.exception;

public class NotificationNotFoundException extends NotFoundException {
  public NotificationNotFoundException(long id) {
    super("Could not find Notification with id: " + id);
  }
}
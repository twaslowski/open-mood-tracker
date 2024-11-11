package de.twaslowski.moodtracker.entity;

public class NotificationSpec {

  public static Notification.NotificationBuilder valid() {
    return Notification.builder()
        .id(1L)
        .active(true)
        .cron("0 0 12 * * *")
        .message("Hello World");
  }

}

package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.domain.entity.Notification;

public class NotificationSpec {

  public static Notification.NotificationBuilder valid() {
    return Notification.builder()
        .active(true)
        .cron("0 0 12 * * *")
        .message("Hello World");
  }

}

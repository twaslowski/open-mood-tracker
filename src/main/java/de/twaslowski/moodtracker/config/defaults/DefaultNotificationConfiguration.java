package de.twaslowski.moodtracker.config.defaults;

import de.twaslowski.moodtracker.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DefaultNotificationConfiguration {

  @Bean
  public Notification defaultNotification(
      @Value("${mood-tracker.telegram.scheduled.notification.recording-reminder.cron}") String cron,
      @Value("${mood-tracker.telegram.scheduled.notification.recording-reminder.text}") String text
  ) {
    return Notification.builder()
        .cron(cron)
        .message(text)
        .active(true)
        .build();
  }
}

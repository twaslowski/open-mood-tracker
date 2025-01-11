package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import de.twaslowski.moodtracker.domain.entity.Notification;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class NotificationScheduler {

  private final TaskScheduler taskScheduler;
  private final NotificationService notificationService;

  public void scheduleNotification(Notification notification) {
    taskScheduler.schedule(() -> notificationService.sendNotification(notification),
        new CronTrigger(notification.getCron()));
  }

  @PostConstruct
  public void scheduleActiveNotifications() {
    notificationService.getActiveNotifications().forEach(this::scheduleNotification);
  }
}

package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import de.twaslowski.moodtracker.entity.Notification;
import de.twaslowski.moodtracker.repository.NotificationRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {

  private final NotificationRepository notificationRepository;
  private final NotificationService notificationService;

  private final TaskScheduler taskScheduler;

  @PostConstruct
  public void scheduleActiveNotifications() {
    List<Notification> activeNotifications = notificationRepository.findAllByActiveIsTrue();
    activeNotifications.forEach(this::scheduleNotification);
  }

  private void scheduleNotification(Notification notification) {
    taskScheduler.schedule(() -> notificationService.sendNotification(notification),
        new CronTrigger(notification.getCron()));
    log.info("Scheduled notification with id {} and cron expression {}", notification.getId(), notification.getCron());
  }
}

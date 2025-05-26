package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.adapter.telegram.scheduled.NotificationScheduler;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
import de.twaslowski.moodtracker.domain.entity.Notification;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.repository.MetricConfigurationRepository;
import de.twaslowski.moodtracker.repository.NotificationRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInitializationService {

  private final UserRepository userRepository;
  private final MetricConfigurationRepository metricConfigurationRepository;
  private final NotificationRepository notificationRepository;

  private final List<Metric> defaultTrackedMetrics;
  private final Notification defaultNotification;
  private final NotificationScheduler notificationScheduler;

  @Transactional
  public User initializeUser(long telegramId) {
    var user = userRepository.save(User.builder()
        .telegramId(telegramId)
        .build());
    trackDefaultMetricsForUser(user);
    initializeDefaultNotification(user);
    log.info("Fully initialized user with id {}", user.getId());
    return user;
  }

  private void trackDefaultMetricsForUser(User user) {
    defaultTrackedMetrics.stream()
        .map(metric -> MetricConfiguration.from(metric, user))
        .forEach(metricConfigurationRepository::save);
  }

  private void initializeDefaultNotification(User user) {
    var notification = notificationRepository.save(
        defaultNotification.toBuilder()
            .userId(user.getId())
            .build()
    );
    notificationScheduler.scheduleNotification(notification);
  }
}

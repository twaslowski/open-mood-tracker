package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.adapter.telegram.scheduled.NotificationScheduler;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.Notification;
import de.twaslowski.moodtracker.domain.entity.TrackedMetric;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.repository.NotificationRepository;
import de.twaslowski.moodtracker.repository.TrackedMetricRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.List;
import java.util.Set;
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
  private final TrackedMetricRepository trackedMetricRepository;
  private final NotificationRepository notificationRepository;

  private final List<Metric> defaultTrackedMetrics;
  private final Notification defaultNotification;
  private final NotificationScheduler notificationScheduler;

  @Transactional
  public User initializeUser(long telegramId) {
    var user = userRepository.save(User.builder()
        .telegramId(telegramId)
        .build());
    log.info("Created user {}", telegramId);
    trackDefaultMetricsForUser(user);
    initializeDefaultNotification(user);
    log.info("Fully initialized user {}", telegramId);
    return user;
  }

  private Set<TrackedMetric> trackDefaultMetricsForUser(User user) {
    return defaultTrackedMetrics.stream()
        .map(metric -> TrackedMetric.from(metric, user))
        .map(trackedMetricRepository::save)
        .collect(Collectors.toSet());
  }

  private void initializeDefaultNotification(User user) {
    var notification = notificationRepository.save(
        defaultNotification.toBuilder()
            .userId(user.getId())
            .build()
    );
    log.info("Created default notification {}", notification);
    notificationScheduler.scheduleNotification(notification);
  }
}

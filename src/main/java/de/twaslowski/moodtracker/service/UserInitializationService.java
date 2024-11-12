package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.entity.Configuration;
import de.twaslowski.moodtracker.entity.Notification;
import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.repository.ConfigurationRepository;
import de.twaslowski.moodtracker.repository.NotificationRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInitializationService {

  private final UserRepository userRepository;
  private final ConfigurationRepository configurationRepository;
  private final NotificationRepository notificationRepository;

  private final List<Metric> defaultMetrics;
  private final List<MetricDatapoint> defaultBaselineConfiguration;
  private final Notification.NotificationBuilder defaultNotification;

  @Transactional
  public boolean initializeUser(long telegramId) {
    var user = userRepository.save(User.builder()
        .telegramId(telegramId)
        .build());
    log.info("Created user {}", telegramId);
    initializeDefaultConfiguration(user);
    initializeDefaultNotification(user);
    log.info("Fully initialized user {}", telegramId);
    return true;
  }

  private void initializeDefaultConfiguration(User user) {
    var defaultMetricIds = defaultMetrics.stream()
        .map(Metric::getId)
        .toList();

    var defaultConfiguration = Configuration.defaults()
        .trackedMetricIds(defaultMetricIds)
        .baselineMetrics(defaultBaselineConfiguration)
        .userId(user.getId())
        .build();

    var configuration = configurationRepository.save(defaultConfiguration);
    log.info("Created default configuration {}", configuration);
  }

  private void initializeDefaultNotification(User user) {
    var notification = notificationRepository.save(
        defaultNotification.userId(user.getTelegramId())
            .build()
    );
    log.info("Created default notification {}", notification);
  }
}

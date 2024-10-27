package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.entity.Configuration;
import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.exception.UserNotFoundException;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final MetricService metricService;

  public boolean createUserFromTelegramId(long telegramId) {
    var defaultMetricIds = metricService.getDefaultMetrics().stream()
        .map(Metric::getId)
        .toList();
    var defaultBaselineConfiguration = metricService.getDefaultBaselineConfiguration();

    var defaultConfiguration = Configuration.defaults()
        .trackedMetricIds(defaultMetricIds)
        .baselineMetrics(defaultBaselineConfiguration)
        .build();

    return userRepository.findByTelegramId(telegramId)
        .map(user -> false)
        .orElseGet(() -> {
          userRepository.save(User.builder()
              .telegramId(telegramId)
              .configuration(defaultConfiguration)
              .build());
          return true;
        });
  }

  public Configuration getUserConfiguration(long userId) {
    return userRepository.findById(userId)
        .map(User::getConfiguration)
        .orElseThrow(() -> new IllegalStateException("User not found"));
  }

  public List<Long> getTrackedMetrics(long userId) {
    return userRepository.findById(userId)
        .map(user -> user.getConfiguration().getTrackedMetricIds())
        .orElseThrow(() -> new UserNotFoundException(userId));
  }

  public User findByTelegramId(long telegramId) {
    return userRepository.findByTelegramId(telegramId)
        .orElseThrow(() -> new UserNotFoundException(telegramId));
  }

  public List<User> findAllUsersWithNotifications() {
    return userRepository.findAllByNotificationsEnabledIsTrue();
  }

  public List<User> findAutoBaselineEligibleUsers() {
    return userRepository.findAll().stream()
        .filter(user -> user.getConfiguration().isAutoBaselineEnabled())
        .filter(user -> !user.getConfiguration().getBaselineMetrics().isEmpty())
        .toList();
  }
}

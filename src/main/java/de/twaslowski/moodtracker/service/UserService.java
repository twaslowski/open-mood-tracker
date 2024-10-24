package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.entity.Configuration;
import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final Set<MetricDatapoint> defaultBaselineConfiguration;

  public boolean createUserFromTelegramId(long telegramId) {
    var defaultConfiguration = Configuration.defaults()
        .baselineConfiguration(defaultBaselineConfiguration)
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

  public User findByTelegramId(long telegramId) {
    return userRepository.findByTelegramId(telegramId)
        .orElseThrow(() -> new IllegalStateException("User not found"));
  }

  public List<User> findAllUsersWithNotifications() {
    return userRepository.findAllByNotificationsEnabledIsTrue();
  }

  public List<User> findAutoBaselineEligibleUsers() {
    return userRepository.findAll().stream()
        .filter(user -> user.getConfiguration().isAutoBaselineEnabled())
        .filter(user -> !user.getConfiguration().getBaselineConfiguration().isEmpty())
        .toList();
  }
}

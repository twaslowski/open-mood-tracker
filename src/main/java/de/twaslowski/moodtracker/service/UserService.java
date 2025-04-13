package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import de.twaslowski.moodtracker.exception.UserNotFoundException;
import de.twaslowski.moodtracker.repository.MetricConfigurationRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final MetricConfigurationRepository metricConfigurationRepository;

  private final UserInitializationService userInitializationService;

  public User createUserFromTelegramId(long telegramId) {
    var existentUser = userRepository.findByTelegramId(telegramId);
    return existentUser.orElseGet(() -> {
      log.info("Creating user from telegramId {}", telegramId);
      var user = userInitializationService.initializeUser(telegramId);
      return userRepository.save(user);
    });
  }

  public List<MetricConfiguration> getTrackedMetrics(User user) {
    return metricConfigurationRepository.findByUserId(user.getId());
  }

  public List<MetricDatapoint> getBaselineConfiguration(String userId) {
    return metricConfigurationRepository.findByUserId(userId).stream()
        .map(MetricConfiguration::defaultDatapoint)
        .toList();
  }

  public User findByTelegramId(long telegramId) {
    return userRepository.findByTelegramId(telegramId)
        .orElseThrow(() -> new UserNotFoundException(telegramId));
  }

  public boolean toggleAutoBaseline(String userId) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    user.toggleAutoBaseline();
    userRepository.save(user);
    return user.isAutoBaselineEnabled();
  }

  public List<User> findAutoBaselineEligibleUsers() {
    return userRepository.findAll().stream()
        .filter(User::isAutoBaselineEnabled)
        .toList();
  }
}

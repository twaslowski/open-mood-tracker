package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.entity.Configuration;
import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.exception.ConfigurationNotFoundException;
import de.twaslowski.moodtracker.exception.UserNotFoundException;
import de.twaslowski.moodtracker.repository.ConfigurationRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final ConfigurationRepository configurationRepository;

  private final UserInitializationService userInitializationService;

  public boolean createUserFromTelegramId(long telegramId) {
    return userRepository.findByTelegramId(telegramId)
        .map(user -> false)
        .orElseGet(() -> userInitializationService.initializeUser(telegramId));
  }

  public Configuration getUserConfiguration(User user) {
    return configurationRepository.findByUserId(user.getId())
        .orElseThrow(() -> new ConfigurationNotFoundException(user.getId()));
  }

  public List<MetricDatapoint> getBaselineConfiguration(long userId) {
    return configurationRepository.findByUserId(userId)
        .map(Configuration::getBaselineMetrics)
        .orElseThrow(() -> new ConfigurationNotFoundException(userId));
  }

  public long getTelegramId(long telegramId) {
    return userRepository.findById(telegramId)
        .map(User::getTelegramId)
        .orElseThrow(() -> new UserNotFoundException(telegramId));
  }

  public User findByTelegramId(long telegramId) {
    return userRepository.findByTelegramId(telegramId)
        .orElseThrow(() -> new UserNotFoundException(telegramId));
  }

  public boolean toggleAutoBaseline(long userId) {
    return configurationRepository.findByUserId(userId)
        .map(Configuration::toggleAutoBaseline)
        .orElseThrow(() -> new ConfigurationNotFoundException(userId));
  }

  public boolean isAutoBaselineEligible(User user) {
    var configuration = getUserConfiguration(user);
    return configuration.isAutoBaselineEnabled()
        && !configuration.getBaselineMetrics().isEmpty();
  }

  public List<User> findAutoBaselineEligibleUsers() {
    return userRepository.findAll().stream()
        .filter(this::isAutoBaselineEligible)
        .toList();
  }
}

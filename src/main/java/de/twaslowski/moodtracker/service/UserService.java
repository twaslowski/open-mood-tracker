package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.domain.entity.TrackedMetric;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import de.twaslowski.moodtracker.exception.UserNotFoundException;
import de.twaslowski.moodtracker.repository.TrackedMetricRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final TrackedMetricRepository trackedMetricRepository;

  private final UserInitializationService userInitializationService;

  public Optional<User> createUserFromTelegramId(long telegramId) {
    var existentUser = userRepository.findByTelegramId(telegramId);
    if (existentUser.isPresent()) {
      return Optional.empty();
    }
    return Optional.of(userInitializationService.initializeUser(telegramId));
  }

  public List<TrackedMetric> getTrackedMetrics(User user) {
    return trackedMetricRepository.findByUserId(user.getId());
  }

  public List<MetricDatapoint> getBaselineConfiguration(String userId) {
    return trackedMetricRepository.findByUserId(userId).stream()
        .map(TrackedMetric::defaultDatapoint)
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

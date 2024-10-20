package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public boolean createUserFromTelegramId(long telegramId) {
    return userRepository.findByTelegramId(telegramId)
        .map(user -> false)
        .orElseGet(() -> {
          userRepository.save(User.builder().telegramId(telegramId).build());
          return true;
        });
  }

  public List<User> findAllUsersWithNotifications() {
    return userRepository.findAllByNotificationsEnabledIsTrue();
  }
}

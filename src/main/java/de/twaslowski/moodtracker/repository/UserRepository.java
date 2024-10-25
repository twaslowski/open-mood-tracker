package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.entity.metric.Metric;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByTelegramId(long telegramId);

  @Query("SELECT c.metrics FROM Configuration c JOIN User WHERE User.telegramId = :telegramId")
  List<Metric> findMetricsForUser(long telegramId);

  @Query("SELECT u FROM User u JOIN u.configuration c WHERE c.notificationsEnabled = true")
  List<User> findAllByNotificationsEnabledIsTrue();
}

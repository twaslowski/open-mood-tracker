package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByTelegramId(long telegramId);
}

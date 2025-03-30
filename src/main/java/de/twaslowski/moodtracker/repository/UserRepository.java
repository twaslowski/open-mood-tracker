package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByTelegramId(long telegramId);
}

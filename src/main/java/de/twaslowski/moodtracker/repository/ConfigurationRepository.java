package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.entity.Configuration;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

  Optional<Configuration> findByUserId(long userId);
}

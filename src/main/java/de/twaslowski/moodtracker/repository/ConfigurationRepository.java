package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.domain.entity.Configuration;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

  Optional<Configuration> findByUserId(long userId);
}

package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.domain.entity.ConfigurationSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationSessionRepository extends JpaRepository<ConfigurationSession, String> {

}

package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

}

package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

}

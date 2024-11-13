package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.domain.entity.Record;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

  List<Record> findByUserId(long id);
}

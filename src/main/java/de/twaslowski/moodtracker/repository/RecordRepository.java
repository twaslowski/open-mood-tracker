package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.domain.entity.Record;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

  List<Record> findByUserId(String id);

  List<Record> findByUserIdAndCreationTimestampAfterAndCreationTimestampBefore(String id, ZonedDateTime from, ZonedDateTime to);
}

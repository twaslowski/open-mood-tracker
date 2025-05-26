package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.domain.entity.Record;
import java.time.LocalDate;
import java.util.List;
import de.twaslowski.moodtracker.domain.entity.Record.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecordRepository extends JpaRepository<Record, Long> {

  List<Record> findByUserId(String id);

  @Query(value = """
      SELECT * FROM record WHERE user_id = :userId
      AND creation_timestamp::date = :date
      AND status = 'COMPLETE'""", nativeQuery = true)
  List<Record> findCompleteRecordByUserAndDate(String userId, @Param("date") LocalDate date);
}

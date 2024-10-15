package de.twaslowski.moodtracker.adapter.telegram.temprecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryRecordRepository extends JpaRepository<TemporaryRecord, Long> {

}

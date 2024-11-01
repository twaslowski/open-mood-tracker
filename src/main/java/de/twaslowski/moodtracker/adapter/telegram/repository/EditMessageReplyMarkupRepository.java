package de.twaslowski.moodtracker.adapter.telegram.repository;

import de.twaslowski.moodtracker.adapter.telegram.dto.value.EditableMarkupMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditMessageReplyMarkupRepository extends JpaRepository<EditableMarkupMessage, Long> {

}

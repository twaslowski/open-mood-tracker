package de.twaslowski.moodtracker.adapter.telegram.editable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EditMessageReplyMarkupRepository extends JpaRepository<EditableMarkupMessage, Long> {

}

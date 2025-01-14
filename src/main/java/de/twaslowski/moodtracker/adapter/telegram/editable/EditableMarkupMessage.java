package de.twaslowski.moodtracker.adapter.telegram.editable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * In order to overwrite the previous Inline Keyboard, message IDs have to be stored.
 * To remain stateless, these message IDs are persisted for any given chat and retrieved
 * when a new InlineKeyboard is to be sent.
 */

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "editable_message")
public class EditableMarkupMessage {

  @Id
  private long chatId;

  @NotNull
  private Integer messageId;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime creationTimestamp;
}

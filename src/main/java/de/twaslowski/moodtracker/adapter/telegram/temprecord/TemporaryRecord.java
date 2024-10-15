package de.twaslowski.moodtracker.adapter.telegram.temprecord;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "temporary_record")
@NoArgsConstructor
public class TemporaryRecord {

  public TemporaryRecord(long telegramId) {
    this.telegramId = telegramId;
  }

  @NotNull
  @Id
  private long telegramId;

  @CreationTimestamp
  private ZonedDateTime creationTimestamp;

  @UpdateTimestamp
  private ZonedDateTime updateTimestamp;

  private Integer mood;

  private Integer sleep;
}

package de.twaslowski.moodtracker.adapter.telegram.temprecord;

import jakarta.persistence.Column;
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
    this.record = "";
  }

  @NotNull
  @Id
  private long telegramId;

  @CreationTimestamp
  @Column(updatable = false, columnDefinition = "timestamp with time zone")
  private ZonedDateTime creationTimestamp;

  @UpdateTimestamp
  private ZonedDateTime updateTimestamp;

  private String record; //json structure
}

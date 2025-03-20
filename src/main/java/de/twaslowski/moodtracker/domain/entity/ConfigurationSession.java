package de.twaslowski.moodtracker.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "configuration_session")
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationSession {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  @NotNull
  @JoinColumn(name = "user_id")
  @ManyToOne
  private User user;

  @NotNull
  @Column(updatable = false)
  private ZonedDateTime createdAt;

  @NotNull
  private ZonedDateTime updatedAt;
}

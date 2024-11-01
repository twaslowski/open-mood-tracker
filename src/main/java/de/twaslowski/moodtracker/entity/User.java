package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
  private long id;

  @NotNull
  private long telegramId;

  // todo remove this from the User entity, create ConfigurationService for connection instead
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "configuration_id", referencedColumnName = "id")
  private Configuration configuration;

  public List<MetricDatapoint> getBaselineConfiguration() {
    return configuration.getBaselineMetrics();
  }

  public boolean toggleAutoBaseline() {
    return configuration.toggleAutoBaseline();
  }
}

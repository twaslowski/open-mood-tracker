package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "configuration")
public class Configuration {

  @Id
  @GeneratedValue(generator = "configuration_id_seq")
  private long id;

  private boolean notificationsEnabled;

  private boolean autoBaselineEnabled;

  @JdbcTypeCode(SqlTypes.JSON)
  private List<MetricDatapoint> baselineConfiguration;

  @JdbcTypeCode(SqlTypes.ARRAY)
  private List<String> metrics;

  public static Configuration.ConfigurationBuilder defaults() {
    return Configuration.builder()
        .baselineConfiguration(List.of(Mood.defaultDatapoint()))
        .notificationsEnabled(true)
        .autoBaselineEnabled(false);
  }

  public boolean toggleAutoBaseline() {
    autoBaselineEnabled = !autoBaselineEnabled;
    return autoBaselineEnabled;
  }
}

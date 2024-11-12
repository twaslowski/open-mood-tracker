package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  private long userId;

  private boolean autoBaselineEnabled;

  @JdbcTypeCode(SqlTypes.JSON)
  private List<MetricDatapoint> baselineMetrics;

  @JdbcTypeCode(SqlTypes.ARRAY)
  private List<Long> trackedMetricIds;

  public static Configuration.ConfigurationBuilder noBaselineEnabled() {
    return Configuration.builder()
        .autoBaselineEnabled(false);
  }

  public boolean toggleAutoBaseline() {
    autoBaselineEnabled = !autoBaselineEnabled;
    return autoBaselineEnabled;
  }
}

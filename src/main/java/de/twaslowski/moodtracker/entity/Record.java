package de.twaslowski.moodtracker.entity;

import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "record")
public class Record {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
  private long id;

  @NotNull
  private long userId;

  @CreationTimestamp
  private ZonedDateTime creationTimestamp;

  @JdbcTypeCode(SqlTypes.JSON)
  @NotNull
  private List<MetricDatapoint> values;

  public List<Long> getIncompleteMetricIds() {
    return values.stream()
        .filter(metric -> metric.value() == null)
        .map(MetricDatapoint::metricId)
        .toList();
  }

  public boolean hasIncompleteMetric() {
    return values.stream().anyMatch(metric -> metric.value() == null);
  }

  public void updateMetric(MetricDatapoint datapoint) {
    this.values = values.stream()
        .map(existingMetric -> existingMetric.metricId() == datapoint.metricId()
            ? datapoint
            : existingMetric)
        .collect(Collectors.toList());
  }
}

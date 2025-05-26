package de.twaslowski.moodtracker.domain.entity;

import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

  public enum Status {
    IN_PROGRESS,
    COMPLETED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_id_seq")
  private long id;

  @NotNull
  private String userId;

  @CreationTimestamp
  private ZonedDateTime creationTimestamp;

  @JdbcTypeCode(SqlTypes.JSON)
  @NotNull
  private List<MetricDatapoint> values;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Status status;

  public List<Long> getIncompleteMetricIds() {
    return values.stream()
        .filter(datapoint -> datapoint.value() == null)
        .map(MetricDatapoint::metricId)
        .toList();
  }

  public boolean hasIncompleteMetric() {
    return values.stream().anyMatch(metric -> metric.value() == null);
  }

  public boolean inProgress() {
    return status == Status.IN_PROGRESS;
  }

  public boolean completed() {
    return status == Status.COMPLETED;
  }

  public void update(MetricDatapoint datapoint) {
    this.values = values.stream().map(m -> m.metricId() == datapoint.metricId()
            ? datapoint
            : m)
        .collect(Collectors.toList());
    if (!hasIncompleteMetric()) {
      this.status = Status.COMPLETED;
    }
  }
}

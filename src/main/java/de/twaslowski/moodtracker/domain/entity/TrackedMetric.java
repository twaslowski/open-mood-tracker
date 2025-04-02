package de.twaslowski.moodtracker.domain.entity;

import static jakarta.persistence.GenerationType.UUID;

import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tracked_metric")
public class TrackedMetric {

  @Id
  @GeneratedValue(strategy = UUID)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "metric_id", nullable = false)
  private Metric metric;

  @NotNull
  private int baselineValue;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdAt;

  @UpdateTimestamp
  private ZonedDateTime updatedAt;

  public static TrackedMetric from(Metric metric, User user) {
    return TrackedMetric.builder()
        .metric(metric)
        .user(user).baselineValue(metric.getDefaultValue())
        .build();
  }

  public MetricDatapoint defaultDatapoint() {
    return MetricDatapoint.builder()
        .metricId(metric.getId())
        .value(baselineValue)
        .build();
  }

  public MetricDatapoint emptyDatapoint() {
    return MetricDatapoint.builder()
        .metricId(metric.getId())
        .value(null)
        .build();
  }
}

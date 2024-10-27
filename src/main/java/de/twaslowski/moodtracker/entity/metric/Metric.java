package de.twaslowski.moodtracker.entity.metric;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Metric {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metric_id_seq")
  private long id;

  @NotNull
  private long ownerId;

  @NotNull
  private String name;

  @NotNull
  private String description;

  @NotNull
  private Integer minValue;

  @NotNull
  private Integer maxValue;

  @JdbcTypeCode(SqlTypes.JSON)
  private Map<Integer, String> labels;

  @CreationTimestamp
  private LocalDateTime creationTimestamp;

  @UpdateTimestamp
  private LocalDateTime updateTimestamp;

  // For sorting labels when creating Callbacks. Defaults to ASC.
  @Enumerated(EnumType.STRING)
  private SortOrder sortOrder;

  private Integer defaultValue;

  public enum SortOrder {
    ASC,
    DESC
  }

  public Comparator<MetricDatapoint> getComparator() {
    return sortOrder.equals(SortOrder.ASC)
        ? Comparator.comparing(MetricDatapoint::value)
        : Comparator.comparing(MetricDatapoint::value).reversed();
  }
}
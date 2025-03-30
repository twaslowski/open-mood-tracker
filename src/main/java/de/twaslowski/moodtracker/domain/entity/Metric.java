package de.twaslowski.moodtracker.domain.entity;

import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import jakarta.persistence.Column;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
  private String ownerId;

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
  @Column(updatable = false)
  private LocalDateTime creationTimestamp;

  @UpdateTimestamp
  private LocalDateTime updateTimestamp;

  // For sorting labels when creating Callbacks. Defaults to ASC.
  @Enumerated(EnumType.STRING)
  private SortOrder sortOrder = SortOrder.ASC;

  private Integer defaultValue;

  public enum SortOrder {
    ASC,
    DESC
  }

  public Map<Integer, String> getLabels() {
    if (labels == null || labels.isEmpty()) {
      return generateLabels();
    }
    return labels;
  }

  private Map<Integer, String> generateLabels() {
    return IntStream.range(minValue, maxValue + 1)
        .boxed()
        .collect(Collectors.toMap(Function.identity(), Object::toString));
  }

  public MetricDatapoint emptyDatapoint() {
    return new MetricDatapoint(id, null);
  }

  public MetricDatapoint defaultDatapoint() {
    return new MetricDatapoint(id, defaultValue);
  }

  public MetricDatapoint datapointWithValue(Integer value) {
    return new MetricDatapoint(id, value);
  }
}
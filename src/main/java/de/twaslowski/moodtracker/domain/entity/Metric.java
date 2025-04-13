package de.twaslowski.moodtracker.domain.entity;

import de.twaslowski.moodtracker.domain.dto.MetricDTO;
import de.twaslowski.moodtracker.domain.value.Label;
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
import java.util.List;
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
  private List<Label> labels;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime creationTimestamp;

  @UpdateTimestamp
  private LocalDateTime updateTimestamp;

  // For sorting labels when creating Callbacks. Defaults to ASC.
  @Enumerated(EnumType.STRING)
  private SortOrder sortOrder = SortOrder.ASC;

  @NotNull
  private boolean defaultMetric;

  private Integer defaultValue;

  public enum SortOrder {
    ASC,
    DESC
  }

  public List<Label> getLabels() {
    if (labels == null || labels.isEmpty()) {
      return generateLabels();
    }
    return labels;
  }

  public Label getLabelFor(int value) {
    return getLabels().stream()
        .filter(label -> label.value() == value)
        .findFirst()
        .orElseThrow();
  }

  private List<Label> generateLabels() {
    return IntStream.range(minValue, maxValue + 1)
        .boxed()
        .map(Label::ofInt)
        .collect(Collectors.toList());
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

  public void updateWith(Metric metric) {
    this.description = metric.description;
    this.minValue = metric.minValue;
    this.maxValue = metric.maxValue;
    this.labels = metric.labels;
    this.sortOrder = metric.sortOrder;
    this.defaultValue = metric.defaultValue;
  }

  public Metric updateWith(MetricDTO metricDTO) {
    this.description = metricDTO.description();
    this.minValue = metricDTO.minValue();
    this.maxValue = metricDTO.maxValue();
    this.labels = metricDTO.labels();
    this.defaultValue = metricDTO.baseline();
    return this;
  }

  public static Metric from(MetricDTO metricDTO, User user) {
    return Metric.builder()
        .name(metricDTO.name())
        .description(metricDTO.description())
        .minValue(metricDTO.minValue())
        .maxValue(metricDTO.maxValue())
        .defaultValue(metricDTO.baseline())
        .defaultMetric(false)
        .ownerId(user.getId())
        .labels(metricDTO.labels())
        .sortOrder(SortOrder.ASC)
        .build();
  }
}
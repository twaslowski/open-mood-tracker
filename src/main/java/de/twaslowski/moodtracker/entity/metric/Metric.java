package de.twaslowski.moodtracker.entity.metric;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Metric {

  @Id
  @GeneratedValue(generator = "metric_id_seq")
  protected long id;

  @NotNull
  protected long ownerId;

  @NotNull
  protected String name;

  @NotNull
  protected String description;

  @NotNull
  protected Integer minValue;

  @NotNull
  protected Integer maxValue;

  @JdbcTypeCode(SqlTypes.JSON)
  protected Map<Integer, String> labels;

  protected Integer defaultValue;

  public MetricDatapoint generateEmptyDatapoint() {
    return new MetricDatapoint(name, null);
  }

  public MetricDatapoint generateDatapoint(Integer value) {
    if (minValue > value || value > maxValue) {
      throw new IllegalArgumentException("Value " + value + " is out of range for metric " + name);
    }
    return new MetricDatapoint(name, value);
  }

  public Comparator<MetricDatapoint> getComparator() {
    return Comparator.comparing(MetricDatapoint::value);
  }
}
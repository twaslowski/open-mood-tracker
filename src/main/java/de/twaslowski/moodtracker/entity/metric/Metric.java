package de.twaslowski.moodtracker.entity.metric;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.twaslowski.moodtracker.config.MetricSerializer;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public abstract class Metric {

  protected final String type;
  protected final int lowerBound;
  protected final int upperBound;
  protected Map<String, Integer> tags;

  protected final Integer value;
}
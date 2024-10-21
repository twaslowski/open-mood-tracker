package de.twaslowski.moodtracker.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import java.io.IOException;

public class MetricDeserializer extends JsonDeserializer<MetricDatapoint> {

  @Override
  public MetricDatapoint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode root = mapper.readTree(p);

    // todo attach different class instances for metrics
    String type = root.get("type").asText();
    Metric metric = null;
    return mapper.treeToValue(root, MetricDatapoint.class);
  }
}
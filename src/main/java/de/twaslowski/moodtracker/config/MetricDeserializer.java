package de.twaslowski.moodtracker.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import java.io.IOException;

public class MetricDeserializer extends JsonDeserializer<Metric> {

  @Override
  public Metric deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode root = mapper.readTree(p);
    String type = root.get("type").asText();

    if (Mood.TYPE.equals(type)) {
      return mapper.treeToValue(root, Mood.class);
    } else if (Sleep.NAME.equals(type)) {
      return mapper.treeToValue(root, Sleep.class);
    } else {
      throw new IllegalArgumentException("Unknown type: " + type);
    }
  }
}
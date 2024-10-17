package de.twaslowski.moodtracker.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.twaslowski.moodtracker.entity.metric.Metric;
import java.io.IOException;

public class MetricSerializer extends StdSerializer<Metric> {

  public MetricSerializer() {
    this(null);
  }

  public MetricSerializer(Class<Metric> t) {
    super(t);
  }

  @Override
  public void serialize(Metric metric, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("type", metric.getClass().getSimpleName().toUpperCase());
    if (metric.getValue() == null) {
      gen.writeNullField("value");
    } else {
      gen.writeNumberField("value", metric.getValue());
    }
    gen.writeEndObject();
  }
}
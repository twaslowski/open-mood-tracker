package de.twaslowski.moodtracker.config.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import java.io.IOException;

public class MetricSerializer extends StdSerializer<MetricDatapoint> {

  public MetricSerializer() {
    this(null);
  }

  public MetricSerializer(Class<MetricDatapoint> t) {
    super(t);
  }

  @Override
  public void serialize(MetricDatapoint datapoint, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("type", datapoint.metric().getName());
    if (datapoint.value() == null) {
      gen.writeNullField("value");
    } else {
      gen.writeNumberField("value", datapoint.value());
    }
    gen.writeEndObject();
  }
}
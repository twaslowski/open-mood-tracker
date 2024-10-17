package de.twaslowski.moodtracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hypersistence.utils.hibernate.type.util.ObjectMapperSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CustomObjectMapperSupplier implements ObjectMapperSupplier {

  private final ObjectMapper objectMapper;

  @Override
  public ObjectMapper get() {
    return objectMapper;
  }
}

package de.twaslowski.moodtracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean(name = "metricMapper")
  public ObjectMapper metricMapper() {
    return new ObjectMapper(new YAMLFactory());
  }
}

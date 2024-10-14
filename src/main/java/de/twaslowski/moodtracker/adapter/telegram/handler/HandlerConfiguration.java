package de.twaslowski.moodtracker.adapter.telegram.handler;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HandlerConfiguration {

  private final StartHandler startHandler;
  private final RecordHandler recordHandler;

  @Bean
  public Collection<UpdateHandler> handlers() {
    return List.of(
        startHandler,
        recordHandler
    );
  }
}

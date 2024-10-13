package de.twaslowski.moodtracker.adapter.telegram.external;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {

  @Bean
  public InMemoryQueue<TelegramUpdate> incomingMessageQueue() {
    return new InMemoryQueue<>();
  }

  @Bean
  public InMemoryQueue<TelegramResponse> outgoingMessageQueue() {
    return new InMemoryQueue<>();
  }
}

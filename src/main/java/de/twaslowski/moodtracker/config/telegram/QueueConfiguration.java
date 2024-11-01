package de.twaslowski.moodtracker.config.telegram;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.dto.value.EditableMarkupMessage;
import java.util.LinkedList;
import java.util.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {

  @Bean
  public Queue<TelegramUpdate> incomingMessageQueue() {
    return new LinkedList<>();
  }

  @Bean
  public Queue<TelegramResponse> outgoingMessageQueue() {
    return new LinkedList<>();
  }

  @Bean
  public Queue<EditableMarkupMessage> messagePersistenceQueue() {
    return new LinkedList<>();
  }
}

package de.twaslowski.moodtracker.adapter.telegram.config;

import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.editable.EditableMarkupMessage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {

  @Bean
  public BlockingQueue<TelegramUpdate> incomingMessageQueue() {
    return new LinkedBlockingQueue<>();
  }

  @Bean
  public BlockingQueue<TelegramResponse> outgoingMessageQueue() {
    return new LinkedBlockingQueue<>();
  }

  @Bean
  public BlockingQueue<EditableMarkupMessage> messagePersistenceQueue() {
    return new LinkedBlockingQueue<>();
  }
}

package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.queue.InMemoryQueue;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramIncomingQueueProcessor {

  private final InMemoryQueue<TelegramUpdate> incomingMessageQueue;
  private final InMemoryQueue<TelegramResponse> outgoingMessageQueue;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @PostConstruct
  public void init() {
    log.info("Starting response processor ...");
    scheduler.scheduleWithFixedDelay(this::sendResponses, 0, 10, TimeUnit.MILLISECONDS);
  }

  public void sendResponses() {
    try {
      var update = incomingMessageQueue.take();
      var response = TelegramResponse.builder()
          .chatId(update.chatId())
          .message("Hello, I'm a bot!")
          .build();
      outgoingMessageQueue.add(response);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Error while sending responses: {}", e.getMessage());
    }
  }
}

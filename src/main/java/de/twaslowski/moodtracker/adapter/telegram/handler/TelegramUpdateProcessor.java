package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.queue.InMemoryQueue;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUpdateProcessor {

  private final InMemoryQueue<TelegramUpdate> incomingMessageQueue;
  private final InMemoryQueue<TelegramResponse> outgoingMessageQueue;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @PostConstruct
  public void init() {
    log.info("Starting incoming queue processor ...");
    scheduler.scheduleWithFixedDelay(this::processUpdate, 0, 10, TimeUnit.MILLISECONDS);
  }

  @SneakyThrows // Not explicitly handling the Queue's InterruptedException
  public void processUpdate() {
    // Processes incoming updates and writes responses to outgoing queue, see TelegramOutgoingQueueProcessor
    var update = incomingMessageQueue.take();
    var response = TelegramResponse.builder()
        .chatId(update.chatId())
        .message("Hello, I'm a bot!")
        .build();
    outgoingMessageQueue.add(response);
  }
}

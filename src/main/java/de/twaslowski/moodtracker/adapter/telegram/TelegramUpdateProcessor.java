package de.twaslowski.moodtracker.adapter.telegram;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.external.InMemoryQueue;
import de.twaslowski.moodtracker.adapter.telegram.handler.TelegramUpdateHandler;
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
public class TelegramUpdateProcessor {

  private final InMemoryQueue<TelegramUpdate> incomingMessageQueue;
  private final InMemoryQueue<TelegramResponse> outgoingMessageQueue;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  private final TelegramUpdateHandler telegramUpdateHandler;

  @PostConstruct
  public void init() {
    log.info("Starting incoming queue processor ...");
    scheduler.scheduleWithFixedDelay(this::processUpdate, 0, 10, TimeUnit.MILLISECONDS);
  }

  public void processUpdate() {
    try {
      var update = incomingMessageQueue.take();
      var response = telegramUpdateHandler.handleUpdate(update);
      outgoingMessageQueue.add(response);
    } catch (InterruptedException e) {
      log.error("Error while processing update: {}", e.getMessage());
    }
  }
}

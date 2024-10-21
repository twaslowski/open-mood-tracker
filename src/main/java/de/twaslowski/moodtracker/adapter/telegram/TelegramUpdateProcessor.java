package de.twaslowski.moodtracker.adapter.telegram;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.queue.InMemoryQueue;
import de.twaslowski.moodtracker.config.LogContext;
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
  private final TelegramUpdateDelegator telegramUpdateDelegator;

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @PostConstruct
  public void init() {
    log.info("Starting incoming queue processor ...");
    scheduler.scheduleWithFixedDelay(this::processUpdate, 0, 10, TimeUnit.MILLISECONDS);
  }

  public void processUpdate() {
    try {
      var update = incomingMessageQueue.take();
      LogContext.enrichWithUpdate(update);

      var response = telegramUpdateDelegator.delegateUpdate(update);
      log.info("Received response for update. Queueing for sending.");

      LogContext.clear();
      outgoingMessageQueue.add(response);
    } catch (Exception e) {
      log.error("Error while processing update: {}", e.getMessage());
    }
  }
}

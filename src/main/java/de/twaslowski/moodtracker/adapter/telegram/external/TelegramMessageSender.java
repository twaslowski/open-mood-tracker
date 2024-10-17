package de.twaslowski.moodtracker.adapter.telegram.external;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class TelegramMessageSender {

  private final InMemoryQueue<TelegramResponse> outgoingMessageQueue;
  private final TelegramClient telegramClient;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @PostConstruct
  public void init() {
    log.info("Starting outgoing queue processor ...");
    scheduler.scheduleWithFixedDelay(this::sendResponses, 0, 10, TimeUnit.MILLISECONDS);
  }

  @SneakyThrows // Not explicitly handling the Queue's InterruptedException
  public void sendResponses() {
    TelegramResponse response = outgoingMessageQueue.take();
    var telegramResponseObject = BotApiMessageFactory.createBotApiMessage(response);
    try {
      telegramClient.execute(telegramResponseObject);
      log.info("Sent response to chat: {}", response.getChatId());
    } catch (TelegramApiException | RuntimeException e) {
      log.error("Error while sending message: {}", e.getMessage());
    }
  }
}
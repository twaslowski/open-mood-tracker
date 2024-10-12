package de.twaslowski.moodtracker.adapter.telegram;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.StartHandler;
import de.twaslowski.moodtracker.adapter.telegram.handler.UnknownUpdateHandler;
import de.twaslowski.moodtracker.adapter.telegram.queue.InMemoryQueue;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class TelegramBotIntegrationTest {

  @Autowired
  private InMemoryQueue<TelegramUpdate> incomingMessageQueue;

  @Autowired
  private InMemoryQueue<TelegramResponse> outgoingMessageQueue;

  @Test
  void shouldRespondToKnownCommand() {
    var update = TelegramUpdate.builder()
        .chatId(1L)
        .text(StartHandler.COMMAND)
        .build();

    // When update is received
    incomingMessageQueue.add(update);

    // Then response should be sent
    await().atMost(3, TimeUnit.SECONDS)
        .untilAsserted(() -> {
          var message = outgoingMessageQueue.take();
          assertThat(message.chatId()).isEqualTo(1L);
          assertThat(message.message()).isEqualTo(StartHandler.RESPONSE);
        });
  }

  @Test
  void shouldRespondWithUnknownMessage() {
    var update = TelegramUpdate.builder()
        .chatId(1L)
        .text("someUnknownCommand")
        .build();

    // When update is received
    incomingMessageQueue.add(update);

    // Then response should be sent
    await().atMost(3, TimeUnit.SECONDS)
        .untilAsserted(() -> {
          var message = outgoingMessageQueue.take();
          assertThat(message.chatId()).isEqualTo(1L);
          assertThat(message.message()).isEqualTo(UnknownUpdateHandler.RESPONSE);
        });
  }

}
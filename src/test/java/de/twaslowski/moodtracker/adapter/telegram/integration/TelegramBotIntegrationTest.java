package de.twaslowski.moodtracker.adapter.telegram.integration;

import static de.twaslowski.moodtracker.adapter.telegram.handler.TelegramUpdateDelegator.UNKNOWN_COMMAND_RESPONSE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.external.InMemoryQueue;
import de.twaslowski.moodtracker.adapter.telegram.handler.StartHandler;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class TelegramBotIntegrationTest extends IntegrationBase {

  @Test
  void shouldRespondToKnownCommand() {
    var update = TelegramUpdate.builder()
        .chatId(1L)
        .text(StartHandler.COMMAND)
        .build();

    incomingMessageQueue.add(update);

    await().atMost(3, TimeUnit.SECONDS)
        .untilAsserted(() -> {
          var message = outgoingMessageQueue.take();
          assertThat(message.chatId()).isEqualTo(1L);
          assertThat(message.message()).isEqualTo(StartHandler.CREATED_RESPONSE);
        });
  }

  @Test
  void shouldRespondWithUnknownMessage() {
    var update = TelegramUpdate.builder()
        .chatId(1L)
        .text("someUnknownCommand")
        .build();

    incomingMessageQueue.add(update);

    await().atMost(3, TimeUnit.SECONDS)
        .untilAsserted(() -> {
          var message = outgoingMessageQueue.take();
          assertThat(message.chatId()).isEqualTo(1L);
          assertThat(message.message()).isEqualTo(UNKNOWN_COMMAND_RESPONSE);
        });
  }
}

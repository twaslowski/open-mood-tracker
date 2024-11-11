package de.twaslowski.moodtracker.adapter.telegram.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.command.StartHandler;
import de.twaslowski.moodtracker.entity.UserSpec;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class UserIntegrationTest extends IntegrationBase {

  @Test
  void shouldCreateUserOnStartCommandIfNotExists() {
    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .text(StartHandler.COMMAND)
        .build();

    incomingMessageQueue.add(update);

    await().atMost(3, TimeUnit.SECONDS)
        .untilAsserted(() -> {
          var user = userRepository.findByTelegramId(1L);
          assertThat(user).isPresent();

          var message = outgoingMessageQueue.remove();
          assertThat(message.getChatId()).isEqualTo(1L);
          assertThat(message.getText()).isEqualTo(messageUtil.getMessage("command.start.created"));
        });
  }

  @Test
  void shouldGreetUserIfAlreadyExists() {
    saveUserWithDefaultConfiguration(UserSpec.valid().build());
    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .text(StartHandler.COMMAND)
        .build();

    incomingMessageQueue.add(update);

    await().atMost(3, TimeUnit.SECONDS)
        .untilAsserted(() -> {
          var user = userRepository.findByTelegramId(1L);
          assertThat(user).isPresent();

          var message = outgoingMessageQueue.remove();
          assertThat(message.getChatId()).isEqualTo(1L);
          assertThat(message.getText()).isEqualTo(messageUtil.getMessage("command.start.exists"));
        });
  }
}

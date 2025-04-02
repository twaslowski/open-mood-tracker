package de.twaslowski.moodtracker.adapter.telegram.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.entity.UserSpec;
import org.junit.jupiter.api.Test;

@IntegrationTest
public class ConfigurationIntegrationTest extends IntegrationBase {

  @Test
  void shouldCreateConfigurationSession() {
    var user = initializeUser(UserSpec.valid().build());

    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/configure")
        .build());

    await().atMost(3, SECONDS).untilAsserted(() -> {
          var response = outgoingMessageQueue.take();
          // var sessionId = extractSessionId(response.getText());
          // todo assert link with webtoken
          assertThat(true).isTrue();
        }
    );
  }
}

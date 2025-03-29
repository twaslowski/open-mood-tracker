package de.twaslowski.moodtracker.adapter.telegram.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.entity.UserSpec;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

@IntegrationTest
public class ConfigurationIntegrationTest extends IntegrationBase {

  @Test
  void shouldCreateConfigurationSession() {
    var user = saveUserWithDefaultConfiguration(UserSpec.valid().build());

    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/configure")
        .build());

    await().atMost(3, SECONDS).untilAsserted(() -> {
          var response = outgoingMessageQueue.take();
          var sessionId = extractSessionId(response.getText());
          var maybeSession = configurationSessionRepository.findById(sessionId).orElseThrow();
          assertThat(maybeSession.getUser().getId()).isEqualTo(user.getId());
        }
    );
  }

  private String extractSessionId(String text) {
    var matcher = Pattern.compile("\\b[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\\b").matcher(text);
    if (matcher.find()) {
      return matcher.group();
    }
    throw new IllegalArgumentException("No session id found in text: " + text);
  }
}

package de.twaslowski.moodtracker.adapter.telegram.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.domain.entity.User.State;
import de.twaslowski.moodtracker.entity.UserSpec;
import org.junit.jupiter.api.Test;

@IntegrationTest
public class SettingsIntegrationTest extends IntegrationBase {

  @Test
  void shouldNotBeAbleToAccessSettingsWhenNotIdle() {
    // given
    var user = saveUserWithDefaultConfiguration(UserSpec.valid().build());
    user.setState(State.RECORDING);

    userRepository.save(user);

    var incomingMessage = TelegramTextUpdate.builder()
        .text("/settings")
        .chatId(user.getTelegramId())
        .build();

    // when
    incomingMessageQueue.add(incomingMessage);

    // then
    await().atMost(3, SECONDS).untilAsserted(() -> {
      assertThat(outgoingMessageQueue).isNotEmpty();
      assertThat(userRepository.findById(user.getId()).get().getState()).isEqualTo(State.RECORDING);

      var response = outgoingMessageQueue.take();
      assertThat(response.getText()).contains(State.RECORDING.toString());
    });
  }
}

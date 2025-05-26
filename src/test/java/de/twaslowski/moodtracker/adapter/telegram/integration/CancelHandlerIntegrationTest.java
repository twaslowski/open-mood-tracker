package de.twaslowski.moodtracker.adapter.telegram.integration;

import static de.twaslowski.moodtracker.domain.entity.Record.Status.IN_PROGRESS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.IntegrationTestBase;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.entity.UserSpec;
import org.junit.jupiter.api.Test;

@IntegrationTest
public class CancelHandlerIntegrationTest extends IntegrationTestBase {

  @Test
  void shouldRemoveOngoingRecord() {
    var user = initializeUser(UserSpec.valid().build());
    // when
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(user.getTelegramId())
        .text("/record")
        .build());

    // then
    await().atMost(5, SECONDS).untilAsserted(() -> {
          var temporaryRecords = recordRepository.findByUserId(user.getId());
          assertThat(temporaryRecords).isNotEmpty();
          var temporaryRecord = temporaryRecords.getFirst();

          assertThat(temporaryRecord.getStatus()).isEqualTo(IN_PROGRESS);
          assertThat(temporaryRecord.getValues()).allMatch(metricDatapoint -> metricDatapoint.value() == null);
        }
    );

    // and cancel
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(user.getTelegramId())
        .text("/cancel")
        .build());

    await().atMost(5, SECONDS).untilAsserted(() -> {
          var temporaryRecords = recordRepository.findByUserId(user.getId());
          assertThat(temporaryRecords).isEmpty();
        }
    );
  }
}

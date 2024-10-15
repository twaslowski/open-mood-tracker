package de.twaslowski.moodtracker.adapter.telegram.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class TemporaryRecordIntegrationTest extends IntegrationBase {

  @Test
  void shouldCreateTemporaryRecordIfNoneExists() {
    assertThat(temporaryRecordRepository.findAll()).isEmpty();

    // when
    incomingMessageQueue.add(TelegramUpdate.builder()
        .chatId(1)
        .text("/record")
        .build());

    // then
    await().untilAsserted(() -> {
          var temporaryRecord = temporaryRecordRepository.findAll().getFirst();
          assertThat(temporaryRecord.getTelegramId()).isEqualTo(1);
          assertThat(temporaryRecord.getMood()).isEqualTo(null);
          assertThat(temporaryRecord.getSleep()).isEqualTo(null);

          var response = outgoingMessageQueue.take();
          assertThat(response.chatId()).isEqualTo(1);
          assertThat(response.message()).isEqualTo("Temporary record 1 successfully created!");
        }
    );
  }
}

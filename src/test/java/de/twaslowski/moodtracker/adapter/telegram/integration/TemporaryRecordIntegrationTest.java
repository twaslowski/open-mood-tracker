package de.twaslowski.moodtracker.adapter.telegram.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class TemporaryRecordIntegrationTest extends IntegrationBase {

  @Test
  void shouldCreateTemporaryRecordIfNoneExists() {
    assertThat(temporaryRecordRepository.findAll()).isEmpty();

    // when
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/record")
        .build());

    // then
    await().untilAsserted(() -> {
          var maybeTemporaryRecord = temporaryRecordRepository.findById(1L);
          assertThat(maybeTemporaryRecord).isPresent();

          var temporaryRecord = maybeTemporaryRecord.get();
          assertThat(temporaryRecord.getTelegramId()).isEqualTo(1);
          assertThat(temporaryRecord.getMood()).isEqualTo(null);
          assertThat(temporaryRecord.getSleep()).isEqualTo(null);

          var response = outgoingMessageQueue.take();
          assertThat(response).isInstanceOf(TelegramInlineKeyboardResponse.class);
        }
    );
  }
}

package de.twaslowski.moodtracker.adapter.telegram.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.entity.metric.Mood;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class TemporaryRecordIntegrationTest extends IntegrationBase {

  @Test
  void shouldCreateTemporaryRecordIfNoneExists() {
    assertThat(recordRepository.findAll()).isEmpty();

    // when
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/record")
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() -> {
          var maybeTemporaryRecord = recordRepository.findByTelegramId(1L);
          assertThat(maybeTemporaryRecord).isNotEmpty();
          var temporaryRecord = maybeTemporaryRecord.getFirst();

          assertThat(temporaryRecord.getTelegramId()).isEqualTo(1);
          assertThat(temporaryRecord.getValues()).isEqualTo(List.of(Mood.empty()));

          var response = (TelegramInlineKeyboardResponse) outgoingMessageQueue.take();
          assertThat(response.getContent()).isEqualTo(Mood.empty().createCallback());
          assertThat(response.getChatId()).isEqualTo(1);
        }
    );
  }
}

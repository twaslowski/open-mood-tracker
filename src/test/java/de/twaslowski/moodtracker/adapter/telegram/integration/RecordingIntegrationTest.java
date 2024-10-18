package de.twaslowski.moodtracker.adapter.telegram.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import java.util.Map;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class RecordingIntegrationTest extends IntegrationBase {

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
          assertThat(temporaryRecord.getValues()).isEqualTo(Set.of(Mood.empty(), Sleep.empty()));

          var response = (TelegramInlineKeyboardResponse) outgoingMessageQueue.take();
          assertThat(response.getContent()).isEqualTo(Map.of(
              "SEVERELY_MANIC", "{\"type\":\"MOOD\",\"value\":3}",
              "MANIC", "{\"type\":\"MOOD\",\"value\":2}",
              "HYPOMANIC", "{\"type\":\"MOOD\",\"value\":1}",
              "NEUTRAL", "{\"type\":\"MOOD\",\"value\":0}",
              "LIGHTLY DEPRESSED", "{\"type\":\"MOOD\",\"value\":-1}",
              "DEPRESSED", "{\"type\":\"MOOD\",\"value\":-2}",
              "SEVERELY_DEPRESSED", "{\"type\":\"MOOD\",\"value\":-3}"
          ));
          assertThat(response.getChatId()).isEqualTo(1);
        }
    );
  }

  @Test
  void shouldDoNothingWhenReceivingAMetricUpdateWhileNotRecording() {
    // when
    assertThat(recordRepository.findAll()).isEmpty();

    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData("{\"type\":\"MOOD\",\"value\":-3}")
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() -> {
          assertThat(recordRepository.findAll()).isEmpty();

          var message = outgoingMessageQueue.take();
          assertThat(message.getChatId()).isEqualTo(1);
          assertThat(message.getMessage()).isEqualTo("You are not recording right now.");
        }
    );
  }

  @Test
  void shouldOverwriteExistingRecordIfUserSubmitsTwice() {
    // when
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/record")
        .build());

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData("{\"type\":\"MOOD\",\"value\":3}")
        .build());

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData("{\"type\":\"MOOD\",\"value\":-3}")
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() -> {
          var maybeTemporaryRecord = recordRepository.findByTelegramId(1L);
          assertThat(maybeTemporaryRecord).isNotEmpty();
          var temporaryRecord = maybeTemporaryRecord.getFirst();

          assertThat(temporaryRecord.getTelegramId()).isEqualTo(1);
          assertThat(temporaryRecord.getValues()).isEqualTo(Set.of(Mood.of(-3), Sleep.empty()));
        }
    );
  }

  @Test
  @SneakyThrows
  void shouldCompleteRecordOnceAllMetricsAreSubmitted() {
    // when
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/record")
        .build());

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData("{\"type\":\"MOOD\",\"value\":0}")
        .build());

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData("{\"type\":\"SLEEP\",\"value\":8}")
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() -> {
          var maybeRecord = recordRepository.findByTelegramId(1L);
          assertThat(maybeRecord).isNotEmpty();
          var record = maybeRecord.getFirst();

          assertThat(record.getTelegramId()).isEqualTo(1);
          assertThat(record.getValues()).isEqualTo(Set.of(Mood.of(0), Sleep.of(8)));
          assertThat(recordService.findIncompleteRecordForTelegramChat(1)).isEmpty();

          outgoingMessageQueue.take();
          outgoingMessageQueue.take();
          var finalResponse = outgoingMessageQueue.take();
          assertThat(finalResponse.getChatId()).isEqualTo(1);
          assertThat(finalResponse.getMessage()).isEqualTo("Record saved.");
        }
    );
  }
}

package de.twaslowski.moodtracker.adapter.telegram.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import de.twaslowski.moodtracker.entity.UserSpec;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class RecordingIntegrationTest extends IntegrationBase {

  @Test
  void shouldCreateTemporaryRecordIfNoneExists() {
    var user = saveUserWithDefaultConfiguration(UserSpec.valid().build());
    // when
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/record")
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() -> {
          var maybeTemporaryRecord = recordRepository.findByUserId(user.getId());
          assertThat(maybeTemporaryRecord).isNotEmpty();
          var temporaryRecord = maybeTemporaryRecord.getFirst();

          assertThat(temporaryRecord.getValues()).isEqualTo(List.of(
              new MetricDatapoint(1, null),
              new MetricDatapoint(2, null)
          ));
        }
    );
  }

  @Test
  @SneakyThrows
  void shouldDoNothingWhenReceivingAMetricUpdateWhileNotRecording() {
    saveUserWithDefaultConfiguration(UserSpec.valid().build());
    // when
    assertThat(recordRepository.findAll()).isEmpty();

    var callback = objectMapper.writeValueAsString(MOOD.datapointWithValue(-3));
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData(callback)
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() ->
        assertThat(recordRepository.findAll()).isEmpty());
  }

  @Test
  @SneakyThrows
  void shouldOverwriteExistingRecordIfUserSubmitsTwice() {
    var user = saveUserWithDefaultConfiguration(UserSpec.valid().build());
    // when
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/record")
        .build());

    var callbackData1 = objectMapper.writeValueAsString(MOOD.datapointWithValue(3));
    var callbackData2 = objectMapper.writeValueAsString(MOOD.datapointWithValue(-3));

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .text("sometext")
        .callbackData(callbackData1)
        .build());

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .text("sometext")
        .callbackData(callbackData2)
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() -> {
          var maybeTemporaryRecord = recordRepository.findByUserId(user.getId());
          assertThat(maybeTemporaryRecord).isNotEmpty();
          var temporaryRecord = maybeTemporaryRecord.getFirst();

          assertThat(temporaryRecord.getValues()).containsAll(Set.of(
              MOOD.datapointWithValue(-3),
              SLEEP.emptyDatapoint()
          ));
        }
    );
  }

  @Test
  @SneakyThrows
  void shouldCompleteRecordOnceAllMetricsAreSubmitted() {
    var user = saveUserWithDefaultConfiguration(UserSpec.valid().build());
    // when
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/record")
        .build());

    // and
    var moodCallback = objectMapper.writeValueAsString(MOOD.defaultDatapoint());
    var sleepCallback = objectMapper.writeValueAsString(SLEEP.defaultDatapoint());
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData(moodCallback)
        .build());

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData(sleepCallback)
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() -> {
          var maybeRecord = recordRepository.findByUserId(user.getId());
          assertThat(maybeRecord).isNotEmpty();
          var record = maybeRecord.getFirst();

          assertThat(record.getValues()).isEqualTo(
              List.of(
                  MOOD.defaultDatapoint(),
                  SLEEP.defaultDatapoint()
              )
          );
          assertThat(recordService.findIncompleteRecordsForUser(user.getId())).isEmpty();
          assertMessageWithExactTextSent(messageUtil.getMessage("command.record.saved", recordService.stringifyRecord(record)));
        }
    );
  }
}

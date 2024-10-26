package de.twaslowski.moodtracker.adapter.telegram.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
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
    var user = userRepository.save(UserSpec.valid().build());
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
              new MetricDatapoint(Mood.NAME, null),
              new MetricDatapoint(Sleep.NAME, null)
          ));
        }
    );
  }

  @Test
  void shouldDoNothingWhenReceivingAMetricUpdateWhileNotRecording() {
    givenUser(UserSpec.valid().build());
    // when
    assertThat(recordRepository.findAll()).isEmpty();

    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData("{\"metricName\":\"MOOD\",\"value\":-3}")
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() ->
        assertThat(recordRepository.findAll()).isEmpty());
  }

  @Test
  void shouldOverwriteExistingRecordIfUserSubmitsTwice() {
    var user = userRepository.save(UserSpec.valid().build());
    // when
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/record")
        .build());

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .text("sometext")
        .callbackData("{\"metricName\":\"MOOD\",\"value\":3}")
        .build());

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .text("sometext")
        .callbackData("{\"metricName\":\"MOOD\",\"value\":-3}")
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() -> {
          var maybeTemporaryRecord = recordRepository.findByUserId(user.getId());
          assertThat(maybeTemporaryRecord).isNotEmpty();
          var temporaryRecord = maybeTemporaryRecord.getFirst();

          assertThat(temporaryRecord.getValues()).containsAll(Set.of(
              new MetricDatapoint(Mood.NAME, -3),
              new MetricDatapoint(Sleep.NAME, null)
          ));
        }
    );
  }

  @Test
  @SneakyThrows
  void shouldCompleteRecordOnceAllMetricsAreSubmitted() {
    var user = userRepository.save(UserSpec.valid().build());
    // when
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .chatId(1)
        .text("/record")
        .build());

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData("{\"metricName\":\"MOOD\",\"value\":0}")
        .build());

    // and
    incomingMessageQueue.add(TelegramInlineKeyboardUpdate.builder()
        .chatId(1)
        .callbackData("{\"metricName\":\"SLEEP\",\"value\":8}")
        .build());

    // then
    await().atMost(3, SECONDS).untilAsserted(() -> {
          var maybeRecord = recordRepository.findByUserId(user.getId());
          assertThat(maybeRecord).isNotEmpty();
          var record = maybeRecord.getFirst();

          assertThat(record.getValues()).isEqualTo(
              List.of(new MetricDatapoint(Mood.NAME, 0),
                  new MetricDatapoint(Sleep.NAME, 8))
          );
          assertThat(recordService.findIncompleteRecordsForUser(user.getId())).isEmpty();
          assertMessageWithTextSent(messageUtil.getMessage("command.record.saved"));
        }
    );
  }
}

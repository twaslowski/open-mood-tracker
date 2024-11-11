package de.twaslowski.moodtracker.adapter.telegram.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.entity.NotificationSpec;
import de.twaslowski.moodtracker.entity.UserSpec;
import java.time.LocalDateTime;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

@IntegrationTest
public class NotificationIntegrationTest extends IntegrationBase {

  @Test
  void shouldScheduleAndSendNotification() {
    // given an existing notification
    var triggerTime = LocalDateTime.now().plusSeconds(2);
    var cron = buildCron(triggerTime);

    var notification = NotificationSpec.valid()
        .cron(cron)
        .build();
    notificationRepository.save(notification);

    var user = UserSpec.valid()
        .id(notification.getUserId())
        .telegramId(1)
        .build();
    userRepository.save(user);

    var expectedMessage = TelegramTextResponse.builder()
        .text(notification.getMessage())
        .chatId(user.getTelegramId())
        .build();

    schedulerConfiguration.scheduleActiveNotifications();

    Awaitility.await().atMost(5, SECONDS).untilAsserted(() -> {
      // then the notification is sent
      assertThat(outgoingMessageQueue).isNotEmpty();
      var message = outgoingMessageQueue.take();

      assertThat(message.getChatId()).isEqualTo(1);
      assertThat(message.getText()).isEqualTo(notification.getMessage());
    });
  }

  private String buildCron(LocalDateTime triggerTime) {
    var minute = triggerTime.getMinute();
    var second = triggerTime.getSecond();
    var hour = triggerTime.getHour();
    return String.format("%d %d %d * * *", second, minute, hour);
  }
}

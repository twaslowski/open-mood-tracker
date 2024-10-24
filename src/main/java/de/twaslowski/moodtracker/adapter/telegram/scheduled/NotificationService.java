package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.queue.InMemoryQueue;
import de.twaslowski.moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

  private final UserService userService;
  private final InMemoryQueue<TelegramResponse> outgoingMessageQueue;

  @Scheduled(cron = "${telegram.notifications.cron}")
  public void sendRecordingReminder() {
    userService.findAllUsersWithNotifications().forEach(user -> {
      log.info("Sending recording reminder to user {}", user.getId());
      outgoingMessageQueue.add(createRecordingReminder(user.getId()));
    });
  }

  private static TelegramResponse createRecordingReminder(long chatId) {
    return TelegramTextResponse.builder()
        .chatId(chatId)
        .text("It's time to record your mood!")
        .build();
  }
}

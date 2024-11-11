package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.entity.Notification;
import de.twaslowski.moodtracker.service.UserService;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {

  private final BlockingQueue<TelegramResponse> outgoingMessageQueue;
  private final UserService userService;

  public void sendNotification(Notification notification) {
    log.info("Sending notification with id {}", notification.getId());
    outgoingMessageQueue.add(createTelegramResponse(notification));
  }

  private TelegramResponse createTelegramResponse(Notification notification) {
    var chatId = userService.getTelegramId(notification.getUserId());
    return TelegramTextResponse.builder()
        .chatId(chatId)
        .text(notification.getMessage())
        .build();
  }
}

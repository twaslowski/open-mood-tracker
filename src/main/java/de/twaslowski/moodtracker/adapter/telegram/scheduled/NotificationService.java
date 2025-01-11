package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.domain.entity.Notification;
import de.twaslowski.moodtracker.exception.UserNotFoundException;
import de.twaslowski.moodtracker.repository.NotificationRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {

  private final BlockingQueue<TelegramResponse> outgoingMessageQueue;
  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;

  public void sendNotification(Notification notification) {
    log.info("Sending notification with id {}", notification.getId());
    outgoingMessageQueue.add(createMessage(notification));
  }

  public List<Notification> getActiveNotifications() {
    return notificationRepository.findAllByActiveIsTrue();
  }

  public List<Notification> getUserNotifications(long userId) {
    return notificationRepository.findAllByUserId(userId);
  }

  private TelegramResponse createMessage(Notification notification) {
    return userRepository.findById(notification.getUserId())
        .map(user -> createResponse(user.getTelegramId(), notification))
        .orElseThrow(() -> new UserNotFoundException(notification.getUserId()));
  }

  private TelegramTextResponse createResponse(long chatId, Notification notification) {
    return TelegramTextResponse.builder()
        .chatId(chatId)
        .text(notification.getMessage())
        .build();
  }
}

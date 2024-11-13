package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.entity.NotificationSpec;
import de.twaslowski.moodtracker.service.UserService;
import java.util.concurrent.BlockingQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotificationTest {

  @Mock
  private UserService userService;

  @Mock
  private BlockingQueue<TelegramResponse> outgoingMessageQueue;

  @InjectMocks
  private NotificationService notificationService;

  @Test
  void shouldNotSendNotificationsWhenNoUsersAreSubscribed() {
    // given
    var notification = NotificationSpec.valid().build();
    when(userService.getTelegramId(notification.getUserId())).thenReturn(1L);

    var message = TelegramTextResponse.builder()
        .text(notification.getMessage())
        .chatId(1)
        .build();

    // when
    notificationService.sendNotification(notification);

    // then
    verify(outgoingMessageQueue).add(message);
  }
}

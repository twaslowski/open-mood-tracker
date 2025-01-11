package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.entity.NotificationSpec;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotificationTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BlockingQueue<TelegramResponse> outgoingMessageQueue;

  @InjectMocks
  private NotificationService notificationService;

  @Test
  void shouldNotSendNotificationsWhenNoUsersAreSubscribed() {
    // given
    var user = UserSpec.valid().build();
    var notification = NotificationSpec.valid().build();
    when(userRepository.findById(notification.getUserId()))
        .thenReturn(Optional.ofNullable(user));

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

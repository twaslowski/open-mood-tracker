package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.service.UserService;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotificationTest {

  @InjectMocks
  private NotificationService notificationService;

  @Mock
  private UserService userService;

  @Mock
  private MessageUtil messageUtil;

  @Mock
  private Queue<TelegramResponse> outgoingMessageQueue;

  @Test
  void shouldNotSendNotificationsWhenNoUsersAreSubscribed() {
    // given
    when(userService.findAllUsersWithNotifications()).thenReturn(Collections.emptyList());

    // when
    notificationService.sendRecordingReminder();

    // then
    verify(outgoingMessageQueue, never()).add(any());
  }

  @Test
  void shouldSendNotificationsWhenUsersAreSubscribed() {
    // given
    var user = User.builder()
        .id(1L)
        .telegramId(1L)
        .notificationsEnabled(true)
        .build();
    when(userService.findAllUsersWithNotifications()).thenReturn(List.of(user));

    // when
    notificationService.sendRecordingReminder();

    // then
    ArgumentCaptor<TelegramResponse> responseCaptor = ArgumentCaptor.forClass(TelegramResponse.class);
    verify(outgoingMessageQueue).add(responseCaptor.capture());

    var response = responseCaptor.getValue();
    assert response.getChatId() == 1L;
    verify(messageUtil).getMessage("notification.reminder");
  }
}

package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AutoBaselineServiceTest {

  @Mock
  private UserService userService;

  @Mock
  private RecordService recordService;

  @Mock
  private MessageUtil messageUtil;

  @Mock
  private BlockingQueue<TelegramResponse> outgoingMessageQueue;

  @InjectMocks
  private AutoBaselineService autoBaselineService;

  @Test
  void shouldCreateBaselineRecordAndSendMessage() {
    var user = UserSpec.valid().build();

    when(userService.findAutoBaselineEligibleUsers()).thenReturn(List.of(user));

    autoBaselineService.createAutoBaselines();

    verify(recordService).recordFromBaseline(user);
    verify(outgoingMessageQueue).add(any());
    verify(messageUtil).getMessage("notification.baseline.created");
  }
}
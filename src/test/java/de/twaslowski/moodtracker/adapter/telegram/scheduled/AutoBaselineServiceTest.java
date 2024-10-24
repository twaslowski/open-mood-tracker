package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.queue.InMemoryQueue;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import java.util.List;
import java.util.Set;
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
  private InMemoryQueue<TelegramResponse> outgoingMessageQueue;

  @InjectMocks
  private AutoBaselineService autoBaselineService;

  @Test
  void shouldCreateBaselineRecordAndSendMessage() {
    var user = UserSpec.valid()
        .baselineConfiguration(Set.of(MetricDatapoint.forMetric(Mood.TYPE)))
        .autoBaselineEnabled(true)
        .build();

    when(userService.findAutoBaselineEligibleUsers()).thenReturn(List.of(user));

    autoBaselineService.createAutoBaselines();

    verify(recordService).fromBaselineConfiguration(user);
    verify(outgoingMessageQueue).add(any());
  }
}
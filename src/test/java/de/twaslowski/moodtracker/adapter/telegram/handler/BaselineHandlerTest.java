package de.twaslowski.moodtracker.adapter.telegram.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BaselineHandlerTest {

  @Mock
  private UserService userService;

  @Mock
  private RecordService recordService;

  @Mock
  private MessageUtil messageUtil;

  @InjectMocks
  private BaselineHandler baselineHandler;

  @Test
  void shouldNotCreateBaselineRecordIfConfigNotPresent() {
    var userWithoutBaselineConfig = User.builder()
        .id(1).telegramId(1).build();
    when(userService.findByTelegramId(1)).thenReturn(userWithoutBaselineConfig);

    var update = TelegramTextUpdate.builder().text("/baseline").chatId(1).build();

    baselineHandler.handleUpdate(update);

    verify(recordService, never()).fromBaselineConfiguration(any());
    verify(messageUtil).getMessage("command.baseline.no-configuration-found");
  }

  @Test
  void shouldCreateBaselineRecord() {
    var userWithBaselineConfig = User.builder()
        .id(1).telegramId(1)
        .baselineConfiguration(Set.of(MetricDatapoint.forMetric(Mood.TYPE)))
        .build();
    when(userService.findByTelegramId(1)).thenReturn(userWithBaselineConfig);

    var update = TelegramTextUpdate.builder().text("/baseline").chatId(1).build();

    baselineHandler.handleUpdate(update);

    verify(recordService).fromBaselineConfiguration(userWithBaselineConfig);
    verify(messageUtil).getMessage("command.baseline.created");
  }
}
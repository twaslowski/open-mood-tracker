package de.twaslowski.moodtracker.adapter.telegram.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.MetricCallbackGenerator;
import de.twaslowski.moodtracker.adapter.telegram.handler.command.RecordHandler;
import de.twaslowski.moodtracker.domain.entity.Record;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.config.defaults.MoodMetric;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordHandlerTest {

  @InjectMocks
  private RecordHandler recordHandler;

  @Mock
  private RecordService recordService;

  @Mock
  private UserService userService;

  @Mock
  private MessageUtil messageUtil;

  @Mock
  private MetricCallbackGenerator metricCallbackGenerator;

  @Test
  void shouldCreateNewRecordIfNoIncompleteRecordExists() {
    var user = UserSpec.valid().build();

    var update = TelegramTextUpdate.builder()
        .text("/record")
        .chatId(1L).build();

    var record = Record.builder()
        .id(1L)
        .values(List.of(MoodMetric.INSTANCE.emptyDatapoint()))
        .build();

    when(userService.findByTelegramId(1L)).thenReturn(user);
    when(recordService.findIncompleteRecordsForUser(1L)).thenReturn(Optional.empty());
    when(recordService.initializeFrom(user)).thenReturn(record);
    when(recordService.getNextIncompleteMetric(any())).thenReturn(Optional.of(MoodMetric.INSTANCE));

    recordHandler.handleUpdate(update);

    verify(recordService).initializeFrom(user);
  }

  @Test
  void shouldHandleExistingIncompleteRecord() {
    var user = UserSpec.valid().build();
    var update = TelegramTextUpdate.builder()
        .text("/record")
        .chatId(1L).build();
    var record = Record.builder().id(1L).build();

    when(userService.findByTelegramId(1L)).thenReturn(user);
    when(recordService.findIncompleteRecordsForUser(1L)).thenReturn(Optional.of(record));
    when(recordService.getNextIncompleteMetric(record)).thenReturn(Optional.of(MoodMetric.INSTANCE));

    recordHandler.handleUpdate(update);

    verify(recordService, never()).initializeFrom(user);
    verify(messageUtil).getMessage("command.record.already-recording");
  }
}
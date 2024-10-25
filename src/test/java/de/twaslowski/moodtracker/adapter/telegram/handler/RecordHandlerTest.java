package de.twaslowski.moodtracker.adapter.telegram.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.CallbackGenerator;
import de.twaslowski.moodtracker.adapter.telegram.handler.command.RecordHandler;
import de.twaslowski.moodtracker.entity.Record;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.service.RecordService;
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
  private MessageUtil messageUtil;

  @Mock
  private CallbackGenerator callbackGenerator;

  @Test
  void shouldCreateNewRecordIfNoIncompleteRecordExists() {
    var update = TelegramTextUpdate.builder()
        .text("/record")
        .chatId(1L).build();

    when(recordService.findIncompleteRecordsForUser(1L)).thenReturn(Optional.empty());

    // Without these, we run into a bunch of NullPointers
    when(recordService.getNextIncompleteMetric(any())).thenReturn(Optional.of(new Mood()));
    when(recordService.initializeFrom(update)).thenReturn(Record.builder().id(1L).build());

    recordHandler.handleUpdate(update);

    verify(recordService).initializeFrom(update);
  }

  @Test
  void shouldHandleExistingIncompleteRecord() {
    var update = TelegramTextUpdate.builder()
        .text("/record")
        .chatId(1L).build();

    var record = Record.builder().id(1L).build();
    when(recordService.findIncompleteRecordsForUser(1L)).thenReturn(Optional.of(record));

    // Without these, we run into a bunch of NullPointers
    when(recordService.getNextIncompleteMetric(record)).thenReturn(Optional.of(new Mood()));

    recordHandler.handleUpdate(update);

    verify(recordService, never()).initializeFrom(update);
    verify(messageUtil).getMessage("command.record.already-recording");
  }
}
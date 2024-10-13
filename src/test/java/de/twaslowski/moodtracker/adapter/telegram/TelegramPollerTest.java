package de.twaslowski.moodtracker.adapter.telegram;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.queue.InMemoryQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
class TelegramPollerTest {

  @InjectMocks
  private TelegramPoller telegramPoller;

  @Mock
  private InMemoryQueue<TelegramUpdate> incomingQueue;

  @Test
  void shouldAddUpdateToIncomingQueue() {
    // todo fix
    when(TelegramUtils.extractUpdate(any())).thenReturn(TelegramUpdate.builder()
        .updateId(1)
        .text("some text")
        .chatId(1)
        .build());

    var update = new Update();

    // When update is processed
    telegramPoller.consume(update);

    // Then telegramUpdate object is added to Queue
    verify(incomingQueue).add(TelegramUpdate.builder()
        .updateId(1)
        .text("some text")
        .chatId(1)
        .build());
  }

  @Test
  void shouldProcessUnknownCommand() {
  }
}
package de.twaslowski.moodtracker.adapter.telegram;

import static org.mockito.Mockito.verify;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.queue.InMemoryQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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
    var telegramUpdate = TelegramUpdate.builder()
        .updateId(1)
        .text("some text")
        .chatId(1)
        .build();
    var update = new Update();

    try (MockedStatic<TelegramUtils> utils = Mockito.mockStatic(TelegramUtils.class)) {
      utils.when(() -> TelegramUtils.extractUpdate(update)).thenReturn(telegramUpdate);
      // When update is processed
      telegramPoller.consume(update);

      // Then telegramUpdate object is added to Queue
      verify(incomingQueue).add(TelegramUpdate.builder()
          .updateId(1)
          .text("some text")
          .chatId(1)
          .build());
    }
  }
}
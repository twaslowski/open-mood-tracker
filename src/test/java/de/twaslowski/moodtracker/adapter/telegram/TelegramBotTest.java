package de.twaslowski.moodtracker.adapter.telegram;

import static org.mockito.Mockito.verify;

import de.twaslowski.moodtracker.adapter.telegram.handler.StartHandler;
import de.twaslowski.moodtracker.adapter.telegram.handler.UnknownUpdateHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

  @Mock
  private StartHandler startHandler;

  @Mock
  private UnknownUpdateHandler unknownUpdateHandler;

  @InjectMocks
  private TelegramBot telegramBot;

  @Test
  void shouldProcessStartCommand() {
    var telegramUpdate = TelegramUpdateSpec.valid()
        .text("/start")
        .build();

    telegramBot.process(telegramUpdate);

    verify(startHandler).handleUpdate(telegramUpdate);
  }

  @Test
  void shouldProcessUnknownCommand() {
    var telegramUpdate = TelegramUpdateSpec.valid()
        .text("unprocessableCommand")
        .build();

    telegramBot.process(telegramUpdate);

    verify(unknownUpdateHandler).handleUpdate(telegramUpdate);
  }
}
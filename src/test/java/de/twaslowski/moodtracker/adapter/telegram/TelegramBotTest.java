package de.twaslowski.moodtracker.adapter.telegram;

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
  private TelegramPoller telegramBot;

  @Test
  void shouldProcessStartCommand() {
  }

  @Test
  void shouldProcessUnknownCommand() {
  }
}
package de.twaslowski.moodtracker.adapter.telegram;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.adapter.telegram.exception.IdleStateRequiredException;
import de.twaslowski.moodtracker.adapter.telegram.handler.command.StartHandler;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TelegramUpdateDelegatorTest {

  @Mock
  private MessageUtil messageUtil;

  private TelegramUpdateDelegator telegramUpdateDelegator;

  @Test
  void shouldReturnUnknownCommandResponse() {
    telegramUpdateDelegator = new TelegramUpdateDelegator(messageUtil, List.of());
    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .updateId(123)
        .text("some text")
        .build();

    // when
    telegramUpdateDelegator.delegateUpdate(update);

    verify(messageUtil).getMessage("error.unknown-command");
  }

  @Test
  void shouldReturnErrorResponse() {
    var startHandler = mock(StartHandler.class);
    when(startHandler.canHandle(any())).thenReturn(true);
    when(startHandler.handleUpdate(any())).thenThrow(new RuntimeException("some error"));

    telegramUpdateDelegator = new TelegramUpdateDelegator(messageUtil, List.of(startHandler));
    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .updateId(123)
        .text("some text")
        .build();

    // when
    var response = telegramUpdateDelegator.delegateUpdate(update);

    verify(messageUtil).getMessage("error.generic");
  }

  @Test
  void shouldReturnIdleStateRequiredResponse() {
    var startHandler = mock(StartHandler.class);

    when(startHandler.canHandle(any())).thenReturn(true);
    when(startHandler.handleUpdate(any())).thenThrow(new IdleStateRequiredException("error message details"));

    telegramUpdateDelegator = new TelegramUpdateDelegator(messageUtil, List.of(startHandler));

    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .updateId(123)
        .text("some text")
        .build();

    // when
    var response = telegramUpdateDelegator.delegateUpdate(update);

    assertThat(response.getText()).isEqualTo("error message details");
  }

}
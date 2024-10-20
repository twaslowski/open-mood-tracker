package de.twaslowski.moodtracker.adapter.telegram;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.StartHandler;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TelegramUpdateDelegatorTest {

  private TelegramUpdateDelegator telegramUpdateDelegator;

  @Test
  void shouldReturnUnknownCommandResponse() {
    telegramUpdateDelegator = new TelegramUpdateDelegator(List.of());
    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .updateId(123)
        .text("some text")
        .build();

    // when
    var response = telegramUpdateDelegator.delegateUpdate(update);

    assertThat(response.getText()).isEqualTo(TelegramResponse.UNKNOWN_COMMAND_RESPONSE);
  }

  @Test
  void shouldReturnErrorResponse() {
    var startHandler = mock(StartHandler.class);
    when(startHandler.canHandle(any())).thenReturn(true);
    when(startHandler.handleUpdate(any())).thenThrow(new RuntimeException("some error"));

    telegramUpdateDelegator = new TelegramUpdateDelegator(List.of(startHandler));
    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .updateId(123)
        .text("some text")
        .build();

    // when
    var response = telegramUpdateDelegator.delegateUpdate(update);

    assertThat(response.getText()).isEqualTo(TelegramResponse.ERROR_RESPONSE);
  }
}
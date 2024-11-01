package de.twaslowski.moodtracker.adapter.telegram.external;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.value.EditableMarkupMessage;
import java.util.LinkedList;
import java.util.Queue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@ExtendWith(MockitoExtension.class)
public class TelegramMessageSenderTest {

  private final TelegramClient telegramClient = mock(TelegramClient.class);
  private final Queue<TelegramResponse> outgoingQueue = new LinkedList<>();
  private final Queue<EditableMarkupMessage> messagePersistenceQueue = new LinkedList<>();

  private final TelegramMessageSender telegramMessageSender =
      new TelegramMessageSender(outgoingQueue, messagePersistenceQueue, telegramClient);

  @Test
  @SneakyThrows
  void shouldSendMessageFromQueue() {
    // Given
    var response = TelegramTextResponse.builder()
        .chatId(1)
        .text("Hello")
        .build();
    outgoingQueue.add(response);

    // When
    telegramMessageSender.sendResponses();

    // Then
    var captor = ArgumentCaptor.forClass(SendMessage.class);
    verify(telegramClient).execute(captor.capture());

    var sendMessage = captor.getValue();
    assert sendMessage.getChatId().equals("1");
    assert sendMessage.getText().equals("Hello");
  }
}

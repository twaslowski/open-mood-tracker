package de.twaslowski.moodtracker.adapter.telegram;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.queue.InMemoryQueue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@ExtendWith(MockitoExtension.class)
public class TelegramMessageSenderTest {

  private TelegramClient telegramClient = mock(TelegramClient.class);

  private final InMemoryQueue<TelegramResponse> outgoingQueue = new InMemoryQueue<>();

  private final TelegramMessageSender telegramMessageSender =
      new TelegramMessageSender(outgoingQueue, telegramClient);

  @Test
  @SneakyThrows
  void shouldSendMessageFromQueue() {
    // Given
    var response = TelegramResponse.builder()
        .chatId(1)
        .message("Hello")
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

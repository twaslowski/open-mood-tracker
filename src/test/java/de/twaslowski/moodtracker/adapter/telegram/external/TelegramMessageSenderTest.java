package de.twaslowski.moodtracker.adapter.telegram.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.editable.EditableMarkupMessage;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@ExtendWith(MockitoExtension.class)
public class TelegramMessageSenderTest {

  @Mock
  private TelegramClient telegramClient;

  @Mock
  private Queue<TelegramResponse> outgoingQueue = new LinkedList<>();

  @Mock
  private LinkedList<EditableMarkupMessage> messagePersistenceQueue = new LinkedList<>();

  @InjectMocks
  private TelegramMessageSender telegramMessageSender;

  @Test
  @SneakyThrows
  void shouldSendMessageFromQueue() {
    // Given
    var response = TelegramTextResponse.builder()
        .chatId(1)
        .text("Hello")
        .build();

    when(outgoingQueue.remove()).thenReturn(response);

    // When
    telegramMessageSender.sendResponses();

    // Then
    var captor = ArgumentCaptor.forClass(SendMessage.class);
    verify(telegramClient).execute(captor.capture());

    var sendMessage = captor.getValue();
    assert sendMessage.getChatId().equals("1");
    assert sendMessage.getText().equals("Hello");
  }

  @Test
  @SneakyThrows
  void shouldAddMessageToEditableMessagePersistenceQueue() {
    // Given
    var response = TelegramInlineKeyboardResponse.builder()
        .chatId(1)
        .text("Hello")
        .content(new LinkedHashMap<>(Map.of("1", "Button")))
        .build();

    var chat = TelegramObjectFactory.chat().build();
    var message = Message.builder()
        .messageId(1)
        .chat(chat)
        .build();

    when(outgoingQueue.remove()).thenReturn(response);
    when(telegramClient.execute(any(SendMessage.class))).thenReturn(message);

    // When
    telegramMessageSender.sendResponses();

    var captor = ArgumentCaptor.forClass(EditableMarkupMessage.class);
    // Then
    verify(messagePersistenceQueue).add(captor.capture());
    var editableMessage = captor.getValue();

    assertThat(editableMessage.getMessageId()).isEqualTo(1);
    assertThat(editableMessage.getChatId()).isEqualTo(1);
  }
}

package de.twaslowski.moodtracker.adapter.telegram.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.editable.EditableMarkupMessage;
import de.twaslowski.moodtracker.adapter.telegram.editable.EditableMarkupMessageService;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@ExtendWith(MockitoExtension.class)
public class TelegramMessageSenderTest {

  private final TelegramClient telegramClient = mock(TelegramClient.class);
  private final MessageUtil messageUtil = mock(MessageUtil.class);
  private final EditableMarkupMessageService editableMarkupMessageService = mock(EditableMarkupMessageService.class);

  private final BlockingQueue<TelegramResponse> outgoingQueue = new LinkedBlockingQueue<>();

  private final BlockingQueue<EditableMarkupMessage> messagePersistenceQueue = new LinkedBlockingQueue<>();

  private final TelegramMessageSender telegramMessageSender = new TelegramMessageSender(
      outgoingQueue,
      messagePersistenceQueue,
      telegramClient,
      editableMarkupMessageService,
      messageUtil
  );

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

    outgoingQueue.add(response);
    when(telegramClient.execute(any(SendMessage.class))).thenReturn(message);

    // When
    telegramMessageSender.sendResponses();

    assertThat(messagePersistenceQueue).hasSize(1);
    var editableMessage = messagePersistenceQueue.take();

    assertThat(editableMessage.getChatId()).isEqualTo(1);
    assertThat(editableMessage.getMessageId()).isEqualTo(1);
  }

  @Test
  @SneakyThrows
  void shouldEditExistingMessageIfExists() {
    var editableMessage = EditableMarkupMessage.builder()
        .chatId(1)
        .messageId(1)
        .build();

    when(editableMarkupMessageService.findMessageForChatId(1))
        .thenReturn(Optional.of(editableMessage));

    var response = TelegramInlineKeyboardResponse.builder()
        .chatId(1)
        .text("Hello")
        .content(new LinkedHashMap<>())
        .build();

    outgoingQueue.add(response);

    ArgumentCaptor<EditMessageReplyMarkup> messageCaptor = ArgumentCaptor.forClass(EditMessageReplyMarkup.class);

    telegramMessageSender.sendResponses();

    verify(telegramClient).execute(messageCaptor.capture());
    assertThat(messageCaptor.getValue().getChatId()).isEqualTo("1");
    assertThat(messageCaptor.getValue().getMessageId()).isEqualTo(1);
  }

  @Test
  @SneakyThrows
  void shouldDeleteEditableMessageUponTerminalAction() {
    var editableMessage = EditableMarkupMessage.builder()
        .chatId(1)
        .messageId(1)
        .build();

    when(editableMarkupMessageService.findMessageForChatId(1))
        .thenReturn(Optional.of(editableMessage));

    var response = TelegramTextResponse.builder()
        .chatId(1)
        .text("Hello")
        .isTerminalAction(true)
        .build();

    outgoingQueue.add(response);

    telegramMessageSender.sendResponses();

    verify(editableMarkupMessageService).deleteMessageForChatId(1);
    verify(telegramClient).execute(any(DeleteMessage.class));
  }
}

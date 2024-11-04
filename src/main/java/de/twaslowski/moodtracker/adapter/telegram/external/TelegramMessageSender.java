package de.twaslowski.moodtracker.adapter.telegram.external;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.editable.EditableMarkupMessage;
import de.twaslowski.moodtracker.adapter.telegram.editable.EditableMarkupMessageService;
import de.twaslowski.moodtracker.adapter.telegram.external.factory.BotApiMessageFactory;
import jakarta.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class TelegramMessageSender {

  private final Queue<TelegramResponse> outgoingMessageQueue;
  private final Queue<EditableMarkupMessage> messagePersistenceQueue;
  private final TelegramClient telegramClient;
  private final EditableMarkupMessageService editableMarkupMessageService;

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @PostConstruct
  public void init() {
    log.info("Starting outgoing queue processor ...");
    scheduler.scheduleWithFixedDelay(this::sendResponses, 0, 50, TimeUnit.MILLISECONDS);
  }

  public void sendResponses() {
    if (!outgoingMessageQueue.isEmpty()) {
      log.info("Processing outgoing response ...");
      var response = outgoingMessageQueue.remove();
      try {
        handleResponse(response);
      } catch (Exception e) {
        log.error("Error processing response", e);
      }
    }
  }

  private void handleResponse(TelegramResponse response) {
    switch (response.getResponseType()) {
      case TEXT -> handleTextResponse((TelegramTextResponse) response);
      case INLINE_KEYBOARD ->
          handleInlineKeyboardResponse((TelegramInlineKeyboardResponse) response);
      default -> throw new IllegalArgumentException("Unknown response type");
    }
  }

  @SneakyThrows
  private void handleTextResponse(TelegramTextResponse response) {
    // editableMarkupMessageService.findMessageForChatId(response.getChatId())
    //     .ifPresent(message -> deleteTemporaryMessage(message));
    telegramClient.execute(BotApiMessageFactory.createTextResponse(response));
    handleAnswerCallbackQuery(response);
  }

  @SneakyThrows
  private void handleInlineKeyboardResponse(TelegramInlineKeyboardResponse response) {
    editableMarkupMessageService.findMessageForChatId(response.getChatId())
        .ifPresentOrElse(
            message -> editExistingInlineKeyboard(response, message),
            () -> createNewInlineKeyboardResponse(response));

    handleAnswerCallbackQuery(response);
  }

  @SneakyThrows
  private void deleteTemporaryMessage(EditableMarkupMessage message) {
    editableMarkupMessageService.deleteMessageForChatId(message.getChatId());
    telegramClient.execute(DeleteMessage.builder()
        .messageId(message.getMessageId())
        .chatId(String.valueOf(message.getChatId()))
        .build());
  }

  @SneakyThrows
  private void handleAnswerCallbackQuery(TelegramResponse response) {
    if (response.hasAnswerCallbackQueryId()) {
      telegramClient.execute(BotApiMessageFactory.createCallbackQueryAnswerResponse(response));
    }
  }

  @SneakyThrows
  private void createNewInlineKeyboardResponse(TelegramInlineKeyboardResponse response) {
    var message = telegramClient.execute(BotApiMessageFactory.createInlineKeyboardResponse(response));
    addToEditableMessagePersistenceQueue(message);
  }

  @SneakyThrows
  private void editExistingInlineKeyboard(TelegramInlineKeyboardResponse response, EditableMarkupMessage message) {
    telegramClient.execute(BotApiMessageFactory.createEditMessageReplyMarkupResponse(response, message));
  }

  private void addToEditableMessagePersistenceQueue(Message message) {
    messagePersistenceQueue.add(
        EditableMarkupMessage.builder()
            .chatId(message.getChatId())
            .messageId(message.getMessageId())
            .build()
    );
  }
}
package de.twaslowski.moodtracker.adapter.telegram.external;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.editable.EditableMarkupMessage;
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
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @PostConstruct
  public void init() {
    log.info("Starting outgoing queue processor ...");
    scheduler.scheduleWithFixedDelay(this::sendResponses, 0, 50, TimeUnit.MILLISECONDS);
  }

  public void sendResponses() {
    if (!outgoingMessageQueue.isEmpty()) {
      var response = outgoingMessageQueue.remove();
      handleResponse(response);
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

  private void handleTextResponse(TelegramTextResponse response) {
    executeUpdate(BotApiMessageFactory.createTextResponse(response));
    handleAnswerCallbackQuery(response);
  }

  private void handleInlineKeyboardResponse(TelegramInlineKeyboardResponse response) {
    var message = executeUpdate(BotApiMessageFactory.createInlineKeyboardResponse(response));
    handleAnswerCallbackQuery(response);
    addToEditableMessagePersistenceQueue(message);
  }

  private void handleAnswerCallbackQuery(TelegramResponse response) {
    if (response.hasAnswerCallbackQueryId()) {
      executeUpdate(BotApiMessageFactory.createCallbackQueryAnswerResponse(response));
    }
  }

  private void addToEditableMessagePersistenceQueue(Message message) {
    messagePersistenceQueue.add(
        EditableMarkupMessage.builder()
            .messageId(message.getMessageId())
            .chatId(message.getChatId())
            .build()
    );
  }

  @SneakyThrows
  private Message executeUpdate(SendMessage update) {
    return telegramClient.execute(update);
  }

  @SneakyThrows
  public void executeUpdate(AnswerCallbackQuery response) {
    telegramClient.execute(response);
  }
}
package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramUpdateHandler {

  private final Collection<UpdateHandler> handlers;
  public static final String UNKNOWN_COMMAND_RESPONSE = "Unfortunately, I cannot process that message.";

  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var relevantHandler = handlers.stream()
        .filter(handler -> handler.canHandle(update))
        .findFirst();

    return relevantHandler
        .map(handler -> handler.handleUpdate(update))
        .orElseGet(() -> {
          log.warn("No handler found for update {}", update);
          return respondToUnknownCommand(update.chatId());
        });
  }

  private TelegramResponse respondToUnknownCommand(long chatId) {
    return TelegramResponse.builder()
        .chatId(chatId)
        .message(UNKNOWN_COMMAND_RESPONSE)
        .build();
  }
}

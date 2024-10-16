package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramUpdateDelegator {

  private final Collection<UpdateHandler> handlers;
  public static final String UNKNOWN_COMMAND_RESPONSE = "Unfortunately, I cannot process that message.";

  public TelegramResponse delegateUpdate(TelegramUpdate update) {
    var relevantHandler = handlers.stream()
        .filter(handler -> handler.canHandle(update))
        .findFirst();

    return relevantHandler
        .map(handler -> handler.handleUpdate(update))
        .orElseGet(() -> {
          log.warn("No handler found for update {}", update);
          return respondToUnknownCommand(update.getChatId());
        });
  }

  private TelegramResponse respondToUnknownCommand(long chatId) {
    return TelegramTextResponse.builder()
        .chatId(chatId)
        .message(UNKNOWN_COMMAND_RESPONSE)
        .build();
  }
}

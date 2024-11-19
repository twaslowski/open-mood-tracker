package de.twaslowski.moodtracker.adapter.telegram;

import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.exception.IdleStateRequiredException;
import de.twaslowski.moodtracker.adapter.telegram.handler.UpdateHandler;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramUpdateDelegator {

  private final MessageUtil messageUtil;
  private final Collection<UpdateHandler> handlers;

  public TelegramResponse delegateUpdate(TelegramUpdate update) {
    var relevantHandler = handlers.stream()
        .filter(handler -> handler.canHandle(update))
        .toList();

    if (relevantHandler.size() > 1) {
      log.warn("Multiple handlers found for update {}: {}", update, relevantHandler);
    }

    return relevantHandler.stream().findFirst()
        .map(handler -> invokeHandler(handler, update))
        .orElseGet(() -> {
          log.warn("No handler found");
          return respondToUnhandleableUpdate(update.getChatId());
        });
  }

  private TelegramResponse invokeHandler(UpdateHandler handler, TelegramUpdate update) {
    try {
      return handler.handleUpdate(update);
    } catch (IdleStateRequiredException e) {
      return TelegramTextResponse.builder()
          .chatId(update.getChatId())
          .text(e.getMessage())
          .build();
    } catch (Exception e) {
      log.error("Error while processing update:", e);
      return TelegramTextResponse.builder()
          .text(messageUtil.getMessage("error.generic"))
          .chatId(update.getChatId())
          .build();
    }
  }

  private TelegramResponse respondToUnhandleableUpdate(long chatId) {
    return TelegramTextResponse.builder()
        .text(messageUtil.getMessage("error.unknown-command"))
        .chatId(chatId)
        .build();
  }
}

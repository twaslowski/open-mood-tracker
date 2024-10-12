package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramUpdateHandler {

  private final StartHandler startHandler;
  private final UnknownUpdateHandler unknownUpdateHandler;

  public TelegramResponse handleUpdate(TelegramUpdate update) {
    return switch (update.text()) {
      case StartHandler.COMMAND -> startHandler.handleUpdate(update);
      default -> unknownUpdateHandler.handleUpdate(update);
    };
  }
}

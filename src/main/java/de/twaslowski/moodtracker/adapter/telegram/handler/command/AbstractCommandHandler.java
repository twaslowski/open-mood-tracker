package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.UpdateHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractCommandHandler implements UpdateHandler {

  protected final MessageUtil messageUtil;

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.getText() != null && update.getText().equals(getCommand());
  }

  public abstract String getCommand();
}

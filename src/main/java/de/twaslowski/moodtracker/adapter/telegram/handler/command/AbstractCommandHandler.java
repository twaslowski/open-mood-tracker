package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.exception.IdleStateRequiredException;
import de.twaslowski.moodtracker.adapter.telegram.handler.UpdateHandler;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.domain.entity.User.State;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractCommandHandler implements UpdateHandler {

  protected final String command;
  protected final MessageUtil messageUtil;

  protected void requireIdleState(User user) {
    if (user.getState() != State.IDLE) {
      throw new IdleStateRequiredException(
          messageUtil.getMessage("error.state.idle-required", user.getState())
      );
    }
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.getText() != null && update.getText().equals(command);
  }
}

package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.SettingsCallbackGenerator;
import de.twaslowski.moodtracker.domain.entity.User.State;
import de.twaslowski.moodtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SettingsHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/settings";

  private final UserService userService;
  private final SettingsCallbackGenerator settingsCallbackGenerator;

  public SettingsHandler(MessageUtil messageUtil,
                         UserService userService,
                         SettingsCallbackGenerator settingsCallbackGenerator) {
    super(messageUtil);
    this.userService = userService;
    this.settingsCallbackGenerator = settingsCallbackGenerator;
  }

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());
    requireIdleState(user);
    userService.transitionUserState(user, State.CONFIGURING);

    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .text("What would you like to edit?")
        .content(settingsCallbackGenerator.createCallback())
        .build();
  }

  @Override
  public String getCommand() {
    return COMMAND;
  }
}

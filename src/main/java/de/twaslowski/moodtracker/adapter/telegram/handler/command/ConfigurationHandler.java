package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.domain.entity.User.State;
import de.twaslowski.moodtracker.service.ConfigurationSessionService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConfigurationHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/configuration";

  private final UserService userService;
  private final ConfigurationSessionService configurationSessionService;

  public ConfigurationHandler(MessageUtil messageUtil,
                              UserService userService,
                              ConfigurationSessionService configurationSessionService) {
    super(messageUtil);
    this.userService = userService;
    this.configurationSessionService = configurationSessionService;
  }

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());
    var configurationSession = configurationSessionService.createSessionFor(user);

    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text("What would you like to edit?")
        .build();
  }

  @Override
  public String getCommand() {
    return COMMAND;
  }
}

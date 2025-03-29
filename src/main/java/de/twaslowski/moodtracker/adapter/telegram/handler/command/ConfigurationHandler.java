package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.service.SessionService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConfigurationHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/configure";
  private static final String RESPONSE = "A <b>temporary session</b> has been created for you. "
      + "You can configure your mood tracker <a href=\"%s\">here</a>.";

  private final UserService userService;
  private final SessionService sessionService;

  public ConfigurationHandler(MessageUtil messageUtil,
                              UserService userService,
                              SessionService sessionService) {
    super(messageUtil);
    this.userService = userService;
    this.sessionService = sessionService;
  }

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());
    var configurationSessionUrl = sessionService.createSessionFor(user);

    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text(RESPONSE.formatted(configurationSessionUrl))
        .build();
  }

  @Override
  public String getCommand() {
    return COMMAND;
  }
}

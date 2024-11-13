package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import org.springframework.stereotype.Service;

@Service
public class HelpHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/help";

  public HelpHandler(MessageUtil messageUtil) {
    super(COMMAND, messageUtil);
  }

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    return TelegramTextResponse.builder()
        .text(messageUtil.getMessage("command.help"))
        .chatId(update.getChatId())
        .build();
  }
}

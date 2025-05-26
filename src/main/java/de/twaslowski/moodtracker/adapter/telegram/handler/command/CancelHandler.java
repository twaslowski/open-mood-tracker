package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.editable.EditableMarkupMessageService;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class CancelHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/cancel";

  private final EditableMarkupMessageService editableMarkupMessageService;
  private final RecordService recordService;
  private final UserService userService;

  public CancelHandler(MessageUtil messageUtil, RecordService recordService, UserService userService, EditableMarkupMessageService editableMarkupMessageService) {
    super(messageUtil);
    this.editableMarkupMessageService = editableMarkupMessageService;
    this.userService = userService;
    this.recordService = recordService;
  }

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    editableMarkupMessageService.deleteMessageForChatId(update.getChatId());
    var user = userService.findByTelegramId(update.getChatId());
    recordService.cancelOngoingRecord(user);

    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text(messageUtil.getMessage("command.canceled"))
        .build();
  }

  @Override
  public String getCommand() {
    return COMMAND;
  }
}

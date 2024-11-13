package de.twaslowski.moodtracker.adapter.telegram.handler.command;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.CallbackGenerator;
import de.twaslowski.moodtracker.adapter.telegram.scheduled.NotificationService;
import de.twaslowski.moodtracker.service.UserService;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationHandler extends AbstractCommandHandler {

  public static final String COMMAND = "/settings";

  private final NotificationService notificationService;
  private final UserService userService;
  private final CallbackGenerator callbackGenerator;

  public NotificationHandler(MessageUtil messageUtil,
                             NotificationService notificationService,
                             UserService userService,
                             CallbackGenerator callbackGenerator) {
    super(COMMAND, messageUtil);
    this.userService = userService;
    this.notificationService = notificationService;
    this.callbackGenerator = callbackGenerator;
  }

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());
    requireIdleState(user);

    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .text("Notifications")
        .content(new LinkedHashMap<>(Map.of()))
        .build();
  }
}

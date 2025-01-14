package de.twaslowski.moodtracker.adapter.telegram.handler.inlinekeyboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.UserAction;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.UpdateHandler;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.NotificationCallbackGenerator;
import de.twaslowski.moodtracker.adapter.telegram.scheduled.NotificationService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EditNotificationsUpdateHandler implements UpdateHandler {

  private final ObjectMapper objectMapper;
  private final UserService userService;
  private final NotificationService notificationService;
  private final NotificationCallbackGenerator notificationCallbackGenerator;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findByTelegramId(update.getChatId());
    var notifications = notificationService.getUserNotifications(user.getId());
    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .text("Which notification would you like to edit?")
        .callbacks(notificationCallbackGenerator.createCallback(notifications))
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    if (update.hasCallback()) {
      var callbackData = ((TelegramInlineKeyboardUpdate) update).getCallbackData();
      return callbackData != null && callbackIsParsable(callbackData);
    } else {
      return false;
    }
  }

  private boolean callbackIsParsable(String callback) {
    try {
      var result = objectMapper.readValue(callback, UserAction.class);
      log.info("Parsed callback data: {}", result);
      return true;
    } catch (JsonProcessingException e) {
      return false;
    }
  }
}

package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.UserAction;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.UserAction.Action;
import de.twaslowski.moodtracker.domain.entity.Notification;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationCallbackGenerator {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public LinkedHashMap<String, String> createCallback(List<Notification> notifications) {
    var callback = new LinkedHashMap<String, String>();
    for (Notification notification : notifications) {
      var userAction = UserAction.of(Action.EDIT_NOTIFICATION, String.valueOf(notification.getId()));
      var callbackData = objectMapper.writeValueAsString(userAction);
      callback.put(notification.getMessage(), callbackData);
    }
    return callback;
  }
}

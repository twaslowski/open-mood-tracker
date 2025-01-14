package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.Callback;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.UserAction;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.UserAction.Action;
import de.twaslowski.moodtracker.domain.entity.Notification;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationCallbackGenerator {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public List<Callback> createCallback(List<Notification> notifications) {
    List<Callback> callbacks = new ArrayList<>();
    for (Notification notification : notifications) {
      var userAction = UserAction.of(Action.EDIT_NOTIFICATION, String.valueOf(notification.getId()));
      var callbackData = objectMapper.writeValueAsString(userAction);
      callbacks.add(new Callback(notification.getMessage(), callbackData));
    }
    return callbacks;
  }
}

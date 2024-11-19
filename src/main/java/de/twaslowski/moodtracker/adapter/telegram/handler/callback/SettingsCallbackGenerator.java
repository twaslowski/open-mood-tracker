package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.UserAction;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.UserAction.Action;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SettingsCallbackGenerator {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public LinkedHashMap<String, String> createCallback() {
    var callback = new LinkedHashMap<String, String>();
    var userAction = UserAction.of(Action.LIST_NOTIFICATIONS);
    var callbackData = objectMapper.writeValueAsString(userAction);
    callback.put("Notifications", callbackData);
    return callback;
  }
}

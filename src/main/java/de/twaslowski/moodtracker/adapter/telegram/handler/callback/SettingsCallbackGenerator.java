package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.Callback;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.UserAction;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.UserAction.Action;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SettingsCallbackGenerator {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public List<Callback> createCallback() {
    var availableActions = List.of(Action.LIST_NOTIFICATIONS, Action.LIST_METRICS);
    var callbacks = new ArrayList<Callback>();
    for (var action : availableActions) {
      var callbackData = objectMapper.writeValueAsString(UserAction.of(action));
      callbacks.add(new Callback(action.getText(), callbackData));
    }
    return callbacks;
  }
}

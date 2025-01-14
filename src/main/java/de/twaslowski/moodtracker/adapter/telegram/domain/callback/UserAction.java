package de.twaslowski.moodtracker.adapter.telegram.domain.callback;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
public class UserAction {

  Action action;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String parameter;

  @Getter
  @RequiredArgsConstructor
  public enum Action {
    LIST_NOTIFICATIONS("Notifications"),
    LIST_METRICS("Metrics"),
    EDIT_NOTIFICATION("Edit Notification");

    private final String text;
  }

  private UserAction(@JsonProperty("action") Action action,
                     @JsonProperty("parameter") String parameter) {
    this.action = action;
    this.parameter = parameter;
  }

  public static UserAction of(Action action) {
    return new UserAction(action, null);
  }

  public static UserAction of(Action action, String parameter) {
    return new UserAction(action, parameter);
  }
}

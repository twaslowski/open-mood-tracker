package de.twaslowski.moodtracker.adapter.telegram.domain.callback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.domain.callback.UserAction.Action;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

class InlineKeyboardCallbackTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @SneakyThrows
  void shouldSerializeEditNotificationsRequest() {
    assertThat(objectMapper.writeValueAsString(UserAction.of(Action.LIST_NOTIFICATIONS)))
        .isEqualTo("{\"action\":\"LIST_NOTIFICATIONS\"}");
  }

  @Test
  @SneakyThrows
  void shouldDeserializeEditNotificationsRequest() {
    var action = objectMapper.readValue("{\"action\":\"LIST_NOTIFICATIONS\"}", UserAction.class);

    assertThat(action.getAction()).isEqualTo(Action.LIST_NOTIFICATIONS);
    assertThat(action.getParameter()).isNull();
  }

  @Test
  @SneakyThrows
  void shouldThrowException() {
    var metricDatapoint = new MetricDatapoint(123, 123);
    var json = objectMapper.writeValueAsString(metricDatapoint);
    assertThat(json).isEqualTo("{\"metricId\":123,\"value\":123}");

    assertThat(objectMapper.readValue(json, MetricDatapoint.class)).isEqualTo(metricDatapoint);

    assertThatThrownBy(() -> objectMapper.readValue(json, UserAction.class))
        .isInstanceOf(JacksonException.class);
  }


  @Test
  @SneakyThrows
  void shouldSerializeEditNotificationRequest() {
    assertThat(objectMapper.writeValueAsString(UserAction.of(Action.EDIT_NOTIFICATION, "123")))
        .isEqualTo("{\"action\":\"EDIT_NOTIFICATION\",\"parameter\":\"123\"}");
  }
}
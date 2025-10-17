package de.twaslowski.moodtracker.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.IntegrationTestBase;
import de.twaslowski.moodtracker.domain.dto.NotificationDTO;
import de.twaslowski.moodtracker.entity.UserSpec;
import lombok.SneakyThrows;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

@IntegrationTest
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  @Qualifier("objectMapper")
  private ObjectMapper mapper;

  private String asJson(Map<String, Object> payload) throws Exception {
    return mapper.writeValueAsString(payload);
  }

  @Test
  @SneakyThrows
  void shouldCreateNotification() {
    var user = initializeUser(UserSpec.valid().build());

    var result = mockMvc.perform(post("/api/v1/notification")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(Map.of(
                "message", "First notification",
                "active", true,
                "cron", "0 0 * * * ?"
            ))))
        .andExpect(status().isOk())
        .andReturn();

    var dto = mapper.readValue(result.getResponse().getContentAsString(), NotificationDTO.class);
    assertThat(dto.id()).isNotNull();
    assertThat(dto.message()).isEqualTo("First notification");
    assertThat(dto.active()).isTrue();
    assertThat(dto.cron()).isEqualTo("0 0 * * * ?");
  }

  @Test
  @SneakyThrows
  void shouldListNotifications() {
    var user = initializeUser(UserSpec.valid().build());

    // Clear default notification
    notificationRepository.deleteAll();

    // Create two notifications
    for (int i = 0; i < 2; i++) {
      mockMvc.perform(post("/api/v1/notification")
              .with(user(user))
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJson(Map.of(
                  "message", "Notification " + i,
                  "active", true,
                  "cron", "0 0 * * * ?"
              ))))
          .andExpect(status().isOk());
    }

    var result = mockMvc.perform(get("/api/v1/notification")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    List<NotificationDTO> dtos = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<NotificationDTO>>() {
    });
    assertThat(dtos).hasSize(2);
  }

  @Test
  @SneakyThrows
  void shouldGetSingleNotification() {
    var user = initializeUser(UserSpec.valid().build());

    var createResult = mockMvc.perform(post("/api/v1/notification")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(Map.of(
                "message", "Get me",
                "active", true,
                "cron", "0 0 * * * ?"
            ))))
        .andExpect(status().isOk())
        .andReturn();
    var created = mapper.readValue(createResult.getResponse().getContentAsString(), NotificationDTO.class);

    var getResult = mockMvc.perform(get("/api/v1/notification/" + created.id())
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    var fetched = mapper.readValue(getResult.getResponse().getContentAsString(), NotificationDTO.class);
    assertThat(fetched.id()).isEqualTo(created.id());
    assertThat(fetched.message()).isEqualTo("Get me");
  }

  @Test
  @SneakyThrows
  void shouldUpdateNotification() {
    var user = initializeUser(UserSpec.valid().build());

    var createResult = mockMvc.perform(post("/api/v1/notification")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(Map.of(
                "message", "Original",
                "active", true,
                "cron", "0 0 * * * ?"
            ))))
        .andExpect(status().isOk())
        .andReturn();
    var created = mapper.readValue(createResult.getResponse().getContentAsString(), NotificationDTO.class);

    var updateResult = mockMvc.perform(put("/api/v1/notification/" + created.id())
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(Map.of(
                "id", created.id(),
                "message", "Updated",
                "active", false,
                "cron", "0 15 * * * ?"
            ))))
        .andExpect(status().isOk())
        .andReturn();

    var updated = mapper.readValue(updateResult.getResponse().getContentAsString(), NotificationDTO.class);
    assertThat(updated.message()).isEqualTo("Updated");
    assertThat(updated.active()).isFalse();
    assertThat(updated.cron()).isEqualTo("0 15 * * * ?");
  }

  @Test
  @SneakyThrows
  void shouldDeleteNotification() {
    var user = initializeUser(UserSpec.valid().build());

    var createResult = mockMvc.perform(post("/api/v1/notification")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(Map.of(
                "message", "To delete",
                "active", true,
                "cron", "0 0 * * * ?"
            ))))
        .andExpect(status().isOk())
        .andReturn();
    var created = mapper.readValue(createResult.getResponse().getContentAsString(), NotificationDTO.class);

    mockMvc.perform(delete("/api/v1/notification/" + created.id())
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    assertThat(notificationRepository.findById(created.id())).isEmpty();
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestOnNotificationNotFound() {
    var user = initializeUser(UserSpec.valid().build());
    long nonexistentId = 9999L;
    assertThat(notificationRepository.findById(nonexistentId)).isEmpty();

    mockMvc.perform(get("/api/v1/notification/" + nonexistentId)
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @SneakyThrows
  void shouldPreventAccessToAnotherUsersNotification() {
    var userA = initializeUser(UserSpec.valid().telegramId(1).build());
    var userB = initializeUser(UserSpec.valid().telegramId(2).build());

    var createResult = mockMvc.perform(post("/api/v1/notification")
            .with(user(userA))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(Map.of(
                "message", "User A notification",
                "active", true,
                "cron", "0 0 * * * ?"
            ))))
        .andExpect(status().isOk())
        .andReturn();
    var created = mapper.readValue(createResult.getResponse().getContentAsString(), NotificationDTO.class);

    mockMvc.perform(get("/api/v1/notification/" + created.id())
            .with(user(userB))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @SneakyThrows
  void shouldRejectInvalidCreatePayload() {
    var user = initializeUser(UserSpec.valid().build());

    mockMvc.perform(post("/api/v1/notification")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(Map.of(
                "active", true
            ))))
        .andExpect(status().isBadRequest());
  }
}


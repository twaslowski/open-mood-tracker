package de.twaslowski.moodtracker.domain.dto;

import de.twaslowski.moodtracker.domain.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record NotificationDTO(
    Long id,
    @NotBlank String message,
    @NotNull Boolean active,
    @NotBlank String cron,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt
) {
  public static NotificationDTO from(Notification notification) {
    return NotificationDTO.builder()
        .id(notification.getId())
        .message(notification.getMessage())
        .active(notification.isActive())
        .cron(notification.getCron())
        .createdAt(notification.getCreatedAt())
        .updatedAt(notification.getUpdatedAt())
        .build();
  }

  public Notification toEntity(String userId) {
    // createdAt/updatedAt managed by JPA
    return Notification.builder()
        .id(id == null ? 0 : id)
        .userId(userId)
        .message(message)
        .active(active)
        .cron(cron)
        .build();
  }
}

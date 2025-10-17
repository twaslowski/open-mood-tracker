package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.domain.dto.NotificationDTO;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.service.NotificationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NotificationController {

  private final NotificationService notificationService;

  @PostMapping("/notification")
  public ResponseEntity<NotificationDTO> createNotification(@AuthenticationPrincipal User user,
                                                            @Valid @RequestBody NotificationDTO notificationDTO) {
    log.info("Creating notification for user {}", user.getId());
    NotificationDTO created = notificationService.create(user, notificationDTO);
    return ResponseEntity.ok(created);
  }

  @GetMapping("/notification")
  public ResponseEntity<List<NotificationDTO>> listNotifications(@AuthenticationPrincipal User user) {
    log.info("Listing notifications for user {}", user.getId());
    return ResponseEntity.ok(notificationService.list(user));
  }

  @GetMapping("/notification/{id}")
  public ResponseEntity<NotificationDTO> getNotification(@AuthenticationPrincipal User user,
                                                         @PathVariable long id) {
    log.info("Getting notification {} for user {}", id, user.getId());
    return ResponseEntity.ok(notificationService.get(user, id));
  }

  @PutMapping("/notification/{id}")
  public ResponseEntity<NotificationDTO> updateNotification(@AuthenticationPrincipal User user,
                                                            @PathVariable long id,
                                                            @Valid @RequestBody NotificationDTO notificationDTO) {
    log.info("Updating notification {} for user {}", id, user.getId());
    return ResponseEntity.ok(notificationService.update(user, id, notificationDTO));
  }

  @DeleteMapping("/notification/{id}")
  public ResponseEntity<Void> deleteNotification(@AuthenticationPrincipal User user,
                                                 @PathVariable long id) {
    log.info("Deleting notification {} for user {}", id, user.getId());
    notificationService.delete(user, id);
    return ResponseEntity.noContent().build();
  }
}

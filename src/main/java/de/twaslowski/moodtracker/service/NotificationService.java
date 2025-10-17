package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.domain.dto.NotificationDTO;
import de.twaslowski.moodtracker.domain.entity.Notification;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.exception.NotificationNotFoundException;
import de.twaslowski.moodtracker.repository.NotificationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("notificationCrudService") // custom bean name to avoid collision with scheduled NotificationService
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;

  @Transactional
  public NotificationDTO create(User user, NotificationDTO dto) {
    log.info("Creating notification for user {}", user.getId());
    Notification notification = dto.toEntity(user.getId());
    Notification saved = notificationRepository.save(notification);
    return NotificationDTO.from(saved);
  }

  @Transactional(readOnly = true)
  public List<NotificationDTO> list(User user) {
    return notificationRepository.findAllByUserId(user.getId()).stream()
        .map(NotificationDTO::from)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public NotificationDTO get(User user, long id) {
    return notificationRepository.findById(id)
        .filter(n -> n.getUserId().equals(user.getId()))
        .map(NotificationDTO::from)
        .orElseThrow(() -> new NotificationNotFoundException(id));
  }

  @Transactional
  public NotificationDTO update(User user, long id, NotificationDTO dto) {
    log.info("Updating notification {} for user {}", id, user.getId());
    Notification existing = notificationRepository.findById(id)
        .filter(n -> n.getUserId().equals(user.getId()))
        .orElseThrow(() -> new NotificationNotFoundException(id));

    existing.setMessage(dto.message());
    existing.setActive(dto.active());
    existing.setCron(dto.cron());

    Notification saved = notificationRepository.save(existing);
    return NotificationDTO.from(saved);
  }

  @Transactional
  public void delete(User user, long id) {
    log.info("Deleting notification {} for user {}", id, user.getId());
    Notification existing = notificationRepository.findById(id)
        .filter(n -> n.getUserId().equals(user.getId()))
        .orElseThrow(() -> new NotificationNotFoundException(id));
    notificationRepository.delete(existing);
  }
}


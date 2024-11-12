package de.twaslowski.moodtracker.repository;

import de.twaslowski.moodtracker.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findAllByActiveIsTrue();

  List<Notification> findAllByUserId(long userId);
}

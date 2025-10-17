package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramTextResponse;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AutoBaselineService {

  private final UserService userService;
  private final RecordService recordService;
  private final BlockingQueue<TelegramResponse> outgoingMessageQueue;
  private final MessageUtil messageUtil;

  @Scheduled(cron = "${mood-tracker.telegram.scheduled.auto-baseline.cron}")
  public void createAutoBaselines() {
    log.info("Starting auto-baseline creation process");
    userService.findAutoBaselineEligibleUsers().stream()
        .filter(this::shouldCreateBaseline)
        .forEach(this::createBaselineRecord);
  }

  private boolean shouldCreateBaseline(User user) {
    return !recordService.userRecordedToday(user);
  }

  private void createBaselineRecord(User user) {
    recordService.recordFromBaseline(user);
    outgoingMessageQueue.add(TelegramTextResponse.builder()
        .text(messageUtil.getMessage("notification.baseline.created"))
        .chatId(user.getTelegramId())
        .build()
    );
    log.info("Baseline record created for user {}", user.getId());
  }
}

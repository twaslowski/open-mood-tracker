package de.twaslowski.moodtracker.adapter.telegram.scheduled;

import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import java.util.Queue;
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
  private final Queue<TelegramResponse> outgoingMessageQueue;
  private final MessageUtil messageUtil;

  @Scheduled(cron = "${telegram.auto-baseline.cron}")
  public void createAutoBaselines() {
    userService.findAutoBaselineEligibleUsers().forEach(user -> {
      recordService.fromBaselineConfiguration(user);
      outgoingMessageQueue.add(TelegramTextResponse.builder()
          .text(messageUtil.getMessage("notification.baseline.created"))
          .chatId(user.getTelegramId())
          .build()
      );
      log.info("Baseline record created for user {}", user.getId());
    });
  }
}

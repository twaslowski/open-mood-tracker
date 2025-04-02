package de.twaslowski.moodtracker.adapter.telegram.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.domain.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.editable.EditableMarkupMessageRepository;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.MetricCallbackGenerator;
import de.twaslowski.moodtracker.adapter.telegram.scheduled.AutoBaselineService;
import de.twaslowski.moodtracker.adapter.telegram.scheduled.NotificationScheduler;
import de.twaslowski.moodtracker.adapter.telegram.scheduled.NotificationService;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.repository.MetricRepository;
import de.twaslowski.moodtracker.repository.NotificationRepository;
import de.twaslowski.moodtracker.repository.RecordRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import java.util.concurrent.BlockingQueue;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class IntegrationBase {

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    recordRepository.deleteAll();
    notificationRepository.deleteAll();

    MOOD = metricRepository.findByName("Mood").orElseThrow();
    SLEEP = metricRepository.findByName("Sleep").orElseThrow();

    outgoingMessageQueue.clear();
    incomingMessageQueue.clear();
  }

  @Autowired
  protected BlockingQueue<TelegramUpdate> incomingMessageQueue;

  @Autowired
  protected BlockingQueue<TelegramResponse> outgoingMessageQueue;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected RecordRepository recordRepository;

  @Autowired
  protected RecordService recordService;

  @Autowired
  protected MetricCallbackGenerator metricCallbackGenerator;

  @Autowired
  protected AutoBaselineService autoBaselineService;

  @Autowired
  protected MessageUtil messageUtil;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected MetricRepository metricRepository;

  @Autowired
  protected EditableMarkupMessageRepository editableMarkupMessageRepository;

  @Autowired
  protected NotificationRepository notificationRepository;

  @Autowired
  protected NotificationService notificationService;

  @Autowired
  protected UserService userService;

  @Autowired
  protected NotificationScheduler notificationScheduler;

  protected Metric MOOD;
  protected Metric SLEEP;

  protected User initializeUser(User user) {
    return userService.createUserFromTelegramId(user.getTelegramId())
        .orElseThrow();
  }

  protected void assertMessageWithExactTextSent(String message) {
    assertThat(outgoingMessageQueue.stream().anyMatch(
        response -> message.equals(response.getText())
    )).isTrue();
  }
}

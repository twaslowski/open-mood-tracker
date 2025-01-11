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
import de.twaslowski.moodtracker.adapter.telegram.scheduled.NotificationService;
import de.twaslowski.moodtracker.adapter.telegram.scheduled.NotificationScheduler;
import de.twaslowski.moodtracker.domain.entity.Configuration;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.entity.ConfigurationSpec;
import de.twaslowski.moodtracker.repository.ConfigurationRepository;
import de.twaslowski.moodtracker.repository.MetricRepository;
import de.twaslowski.moodtracker.repository.NotificationRepository;
import de.twaslowski.moodtracker.repository.RecordRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import java.util.concurrent.BlockingQueue;
import org.junit.jupiter.api.AfterEach;
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
    configurationRepository.deleteAll();

    MOOD = metricRepository.findByName("Mood").orElseThrow();
    SLEEP = metricRepository.findByName("Sleep").orElseThrow();

    outgoingMessageQueue.clear();
    incomingMessageQueue.clear();
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    recordRepository.deleteAll();
    configurationRepository.deleteAll();
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
  protected ConfigurationRepository configurationRepository;

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
  protected NotificationScheduler notificationScheduler;

  @Autowired
  protected NotificationService notificationService;

  @Autowired
  protected UserService userService;

  protected Metric MOOD;
  protected Metric SLEEP;

  protected User saveUserWithDefaultConfiguration(User user) {
    user = userRepository.save(user);
    var configuration = ConfigurationSpec.valid()
        .userId(user.getId())
        .build();
    configurationRepository.save(configuration);
    return user;
  }

  protected User saveUserWithConfiguration(User user, Configuration configuration) {
    user = userRepository.save(user);
    configuration.setUserId(user.getId());
    configurationRepository.save(configuration);
    return user;
  }

  protected void assertMessageWithExactTextSent(String message) {
    assertThat(outgoingMessageQueue.stream().anyMatch(
        response -> message.equals(response.getText())
    )).isTrue();
  }

  protected void assertMessageContainingTextSent(String message) {
    assertThat(outgoingMessageQueue.stream().anyMatch(
        response -> message.contains(response.getText())
    )).isTrue();
  }
}

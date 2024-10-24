package de.twaslowski.moodtracker.adapter.telegram.integration;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.CallbackGenerator;
import de.twaslowski.moodtracker.adapter.telegram.queue.InMemoryQueue;
import de.twaslowski.moodtracker.adapter.telegram.scheduled.AutoBaselineService;
import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.repository.RecordRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import de.twaslowski.moodtracker.service.RecordService;
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
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    recordRepository.deleteAll();
  }

  @Autowired
  protected InMemoryQueue<TelegramUpdate> incomingMessageQueue;

  @Autowired
  protected InMemoryQueue<TelegramResponse> outgoingMessageQueue;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected RecordRepository recordRepository;

  @Autowired
  protected RecordService recordService;

  @Autowired
  protected CallbackGenerator callbackGenerator;

  @Autowired
  protected AutoBaselineService autoBaselineService;

  protected void givenUser(long chatId) {
    userRepository.save(User.builder()
        .telegramId(chatId)
        .build());
  }
}

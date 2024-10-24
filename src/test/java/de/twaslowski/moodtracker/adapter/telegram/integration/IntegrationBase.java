package de.twaslowski.moodtracker.adapter.telegram.integration;

import static org.assertj.core.api.Assertions.assertThat;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.CallbackGenerator;
import de.twaslowski.moodtracker.adapter.telegram.scheduled.AutoBaselineService;
import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.repository.RecordRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import de.twaslowski.moodtracker.service.RecordService;
import java.util.Queue;
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
  protected Queue<TelegramUpdate> incomingMessageQueue;

  @Autowired
  protected Queue<TelegramResponse> outgoingMessageQueue;

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

  @Autowired
  protected MessageUtil messageUtil;

  protected User givenUser(long chatId) {
    return userRepository.save(
        UserSpec.valid()
            .telegramId(chatId)
            .build()
    );
  }

  protected void assertMessageWithTextSent(String message) {
    assertThat(outgoingMessageQueue.stream().anyMatch(
        response -> message.equals(response.getText())
    )).isTrue();
  }
}

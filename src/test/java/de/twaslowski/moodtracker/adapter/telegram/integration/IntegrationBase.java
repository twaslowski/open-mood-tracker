package de.twaslowski.moodtracker.adapter.telegram.integration;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.external.InMemoryQueue;
import de.twaslowski.moodtracker.adapter.telegram.temprecord.TemporaryRecordRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class IntegrationBase {

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    temporaryRecordRepository.deleteAll();
  }

  @Autowired
  protected InMemoryQueue<TelegramUpdate> incomingMessageQueue;

  @Autowired
  protected InMemoryQueue<TelegramResponse> outgoingMessageQueue;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected TemporaryRecordRepository temporaryRecordRepository;
}

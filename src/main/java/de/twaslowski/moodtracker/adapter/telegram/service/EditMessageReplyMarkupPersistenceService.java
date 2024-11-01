package de.twaslowski.moodtracker.adapter.telegram.service;

import de.twaslowski.moodtracker.adapter.telegram.dto.value.EditableMarkupMessage;
import de.twaslowski.moodtracker.adapter.telegram.repository.EditMessageReplyMarkupRepository;
import jakarta.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EditMessageReplyMarkupPersistenceService {

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  private final EditMessageReplyMarkupRepository editMessageReplyMarkupRepository;
  private final Queue<EditableMarkupMessage> editableMarkupMessageQueue;

  @PostConstruct
  public void init() {
    log.info("Starting outgoing queue processor ...");
    scheduler.scheduleWithFixedDelay(this::persistEditMessageReplyMarkup, 0, 50, TimeUnit.MILLISECONDS);
  }

  private void persistEditMessageReplyMarkup() {
    if (!editableMarkupMessageQueue.isEmpty()) {
      var message = editableMarkupMessageQueue.remove();
      editMessageReplyMarkupRepository.save(message);
    }
  }
}

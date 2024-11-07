package de.twaslowski.moodtracker.adapter.telegram.editable;

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
public class EditableMarkupMessagePersistenceService {

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  private final Queue<EditableMarkupMessage> editableMarkupMessageQueue;
  private final EditableMarkupMessageService editableMarkupMessageService;

  @PostConstruct
  public void init() {
    log.info("Starting outgoing queue processor ...");
    scheduler.scheduleWithFixedDelay(this::persistEditMessageReplyMarkup, 0, 50, TimeUnit.MILLISECONDS);
  }

  private void persistEditMessageReplyMarkup() {
    if (!editableMarkupMessageQueue.isEmpty()) {
      var message = editableMarkupMessageQueue.remove();
      editableMarkupMessageService.persist(message);
    }
  }
}

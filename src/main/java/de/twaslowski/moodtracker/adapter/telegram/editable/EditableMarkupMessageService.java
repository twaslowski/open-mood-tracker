package de.twaslowski.moodtracker.adapter.telegram.editable;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EditableMarkupMessageService {

  private final EditableMarkupMessageRepository editableMarkupMessageRepository;

  public Optional<EditableMarkupMessage> findMessageForChatId(long chatId) {
    return editableMarkupMessageRepository.findById(chatId);
  }

  public void persist(EditableMarkupMessage message) {
    log.info("Persisting editable message id {} for chat {}",
        message.getMessageId(), message.getChatId());
    editableMarkupMessageRepository.save(message);
  }
}

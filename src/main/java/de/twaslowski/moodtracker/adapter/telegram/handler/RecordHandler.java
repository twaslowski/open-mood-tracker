package de.twaslowski.moodtracker.adapter.telegram.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.service.RecordService;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordHandler implements UpdateHandler {

  public static final String COMMAND = "/record";

  private final RecordService recordService;
  private final ObjectMapper objectMapper;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    log.info("{}: Handling record command.", update.getChatId());
    var temporaryRecord = recordService.initializeFrom(update);
    var metric = temporaryRecord.getFirstIncompleteMetric().orElseThrow(
        () -> new IllegalStateException("No incomplete metric found in freshly created record.")
    );
    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .content(createCallbacks(metric))
        .message("Temporary record created. First metric ...")
        .build();
  }

  private Map<String, String> createCallbacks(Metric metric) {
    return metric.getTags().entrySet().stream()
        .map(entry -> Map.entry(entry.getKey(), safeWriteValueAsString(entry.getValue())))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @SneakyThrows
  private String safeWriteValueAsString(Metric metric) {
    return objectMapper.writeValueAsString(metric);
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return COMMAND.equals(update.getText());
  }
}

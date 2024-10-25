package de.twaslowski.moodtracker.adapter.telegram.handler;

import de.twaslowski.moodtracker.adapter.telegram.handler.command.AutoBaselineHandler;
import de.twaslowski.moodtracker.adapter.telegram.handler.command.BaselineHandler;
import de.twaslowski.moodtracker.adapter.telegram.handler.command.RecordHandler;
import de.twaslowski.moodtracker.adapter.telegram.handler.command.StartHandler;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HandlerConfiguration {

  private final StartHandler startHandler;
  private final RecordHandler recordHandler;
  private final InlineKeyboardUpdateHandler inlineKeyboardUpdateHandler;
  private final BaselineHandler baselineHandler;
  private final AutoBaselineHandler autoBaselineHandler;

  @Bean
  public Collection<UpdateHandler> handlers() {
    return List.of(
        startHandler,
        recordHandler,
        inlineKeyboardUpdateHandler,
        baselineHandler,
        autoBaselineHandler
    );
  }
}

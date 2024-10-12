package de.twaslowski.moodtracker;

import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class MoodTrackerTestApp extends MoodTracker {

  @Bean
  public TelegramClient telegramClient() {
    return null;
  }
}

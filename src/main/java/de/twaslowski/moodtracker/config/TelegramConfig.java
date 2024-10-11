package de.twaslowski.moodtracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfig {

  @Bean
  public TelegramClient telegramClient(@Value("${telegram.bot.token}") String token) {
    // This appears to be redundant with the getBotToken() method in TelegramBot
    // However, both have to be provided, otherwise either the initial bot registration
    // or the actual message sending will fail.
    return new OkHttpTelegramClient(token);
  }
}

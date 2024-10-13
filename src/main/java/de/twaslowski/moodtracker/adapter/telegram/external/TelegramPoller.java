package de.twaslowski.moodtracker.adapter.telegram.external;

import static de.twaslowski.moodtracker.adapter.telegram.TelegramUtils.extractUpdate;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class TelegramPoller implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

  private final InMemoryQueue<TelegramUpdate> incomingMessageQueue;

  @Value("${telegram.bot.token}")
  private String botToken;

  @Override
  public void consume(Update update) {
    log.info("Received update: {}", update.getUpdateId());
    var telegramUpdate = extractUpdate(update);

    log.info("Queueing update: {}", telegramUpdate);
    incomingMessageQueue.add(telegramUpdate);
  }

  // SpringLongPollingBot boilerplate
  @Override
  public String getBotToken() {
    return botToken;
  }

  @Override
  public LongPollingUpdateConsumer getUpdatesConsumer() {
    return this;
  }

  @AfterBotRegistration
  public void afterRegistration(BotSession botSession) {
    log.info("Successfully registered Telegram bot. Starting polling ...");
  }
}

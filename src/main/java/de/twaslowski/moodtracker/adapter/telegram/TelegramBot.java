package de.twaslowski.moodtracker.adapter.telegram;

import static de.twaslowski.moodtracker.adapter.telegram.TelegramUtils.extractUpdate;

import de.twaslowski.moodtracker.adapter.telegram.dto.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.StartHandler;
import de.twaslowski.moodtracker.adapter.telegram.handler.UnknownUpdateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

  private final StartHandler startHandler;
  private final UnknownUpdateHandler unknownUpdateHandler;

  @Value("${telegram.bot.token}")
  private String botToken;

  @Override
  public void consume(Update update) {
    log.info("Received update: {}", update.getUpdateId());
    var telegramUpdate = extractUpdate(update);

    process(telegramUpdate);
  }

  public void process(TelegramUpdate telegramUpdate) {
    switch (telegramUpdate.text()) {
      case StartHandler.COMMAND -> startHandler.handleUpdate(telegramUpdate);
      default -> unknownUpdateHandler.handleUpdate(telegramUpdate);
    }
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

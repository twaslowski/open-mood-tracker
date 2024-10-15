package de.twaslowski.moodtracker.adapter.telegram.dto.update;

import lombok.Builder;

@Builder
public record TelegramUpdate(long updateId,
                             long chatId,
                             String text) {

}

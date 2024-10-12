package de.twaslowski.moodtracker.adapter.telegram.dto;

import lombok.Builder;

@Builder
public record TelegramResponse(
    long chatId,
    String message
) {

}

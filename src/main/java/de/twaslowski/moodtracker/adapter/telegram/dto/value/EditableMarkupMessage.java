package de.twaslowski.moodtracker.adapter.telegram.dto.value;

import lombok.Builder;

@Builder
public record EditableMarkupMessage(
    long chatId,
    int messageId
) {



}

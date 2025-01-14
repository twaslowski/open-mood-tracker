package de.twaslowski.moodtracker.adapter.telegram.domain.callback;

public record Callback(
    String text,
    Object data
) {

}

package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import java.util.Comparator;
import java.util.List;
import lombok.Builder;

@Builder
public record CallbackContainer(
    List<Callback> callbacks,
    Comparator<Callback> comparator
) {

}

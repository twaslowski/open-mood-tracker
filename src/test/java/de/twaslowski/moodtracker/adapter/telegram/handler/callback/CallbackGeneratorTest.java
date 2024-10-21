package de.twaslowski.moodtracker.adapter.telegram.handler.callback;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.entity.metric.Mood;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CallbackGeneratorTest {

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private CallbackGenerator callbackGenerator;

  @Test
  void shouldThrowIllegalArgumentExceptionOnUnknownMetricType() {
    var metric = mock(Mood.class);
    when(metric.getName()).thenReturn("unknown");

    assertThrows(IllegalArgumentException.class, () -> callbackGenerator.createCallbacks(metric));
  }
}

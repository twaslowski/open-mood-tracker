package de.twaslowski.moodtracker.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.domain.entity.ConfigurationSession;
import de.twaslowski.moodtracker.exception.SessionExpiredException;
import de.twaslowski.moodtracker.exception.SessionNotFoundException;
import de.twaslowski.moodtracker.repository.ConfigurationSessionRepository;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

  @Mock
  private ConfigurationSessionRepository sessionRepository;

  @InjectMocks
  private SessionService sessionService;

  @Test
  void shouldThrowSessionNotFoundException() {
    when(sessionRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> sessionService.retrieveSession("123"))
        .isInstanceOf(SessionNotFoundException.class);
  }

  @Test
  void shouldThrowSessionExpiredException() {
    when(sessionRepository.findById(anyString())).thenReturn(Optional.of(ConfigurationSession.builder()
        .sessionExpiry(ZonedDateTime.now().minusSeconds(1))
        .build()));

    assertThatThrownBy(() -> sessionService.retrieveSession("123"))
        .isInstanceOf(SessionExpiredException.class);
  }
}
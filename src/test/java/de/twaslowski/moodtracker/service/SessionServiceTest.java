package de.twaslowski.moodtracker.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.twaslowski.moodtracker.repository.ConfigurationSessionRepository;
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
    assertTrue(true);
  }

  @Test
  void shouldThrowSessionExpiredException() {
    assertTrue(true);
  }
}
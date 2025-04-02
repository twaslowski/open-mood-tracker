package de.twaslowski.moodtracker.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.repository.TrackedMetricRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserInitializationService userInitializationService;

  @Mock
  private TrackedMetricRepository trackedMetricRepository;

  @InjectMocks
  private UserService userService;

  @Test
  void shouldReturnTrueOnUserCreation() {
    when(userRepository.findByTelegramId(1L)).thenReturn(Optional.empty());

    userService.createUserFromTelegramId(1L);

    verify(userInitializationService).initializeUser(1L);
  }
}
package de.twaslowski.moodtracker.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.entity.User;
import de.twaslowski.moodtracker.repository.ConfigurationRepository;
import de.twaslowski.moodtracker.repository.UserRepository;
import java.util.Optional;
import org.hibernate.engine.config.spi.ConfigurationService;
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
  private MetricService metricService;

  @Mock
  private ConfigurationRepository configurationRepository;

  @InjectMocks
  private UserService userService;

  @Test
  void shouldReturnTrueOnUserCreation() {
    when(userRepository.findByTelegramId(1L)).thenReturn(Optional.empty());

    assertTrue(userService.createUserFromTelegramId(1L));
  }

  @Test
  void shouldReturnFalseOnUserCreation() {
    when(userRepository.findByTelegramId(1L)).thenReturn(Optional.of(new User()));

    assertFalse(userService.createUserFromTelegramId(1L));
  }
}
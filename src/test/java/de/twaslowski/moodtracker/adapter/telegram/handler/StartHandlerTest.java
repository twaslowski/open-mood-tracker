package de.twaslowski.moodtracker.adapter.telegram.handler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StartHandlerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private StartHandler startHandler;

  @Test
  void shouldReturnCreatedResponseIfUserCreated() {
    // given
    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .text(StartHandler.COMMAND)
        .build();

    when(userService.createUserFromTelegramId(1L)).thenReturn(true);

    // when
    var response = startHandler.handleUpdate(update);

    // then
    assertThat(response.getChatId()).isEqualTo(1L);
    assertThat(response.getMessage()).isEqualTo(StartHandler.CREATED_RESPONSE);
  }

  @Test
  void shouldReturnExistsResponseIfUserExists() {
    // given
    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .text(StartHandler.COMMAND)
        .build();

    when(userService.createUserFromTelegramId(1L)).thenReturn(false);

    // when
    var response = startHandler.handleUpdate(update);

    // then
    assertThat(response.getChatId()).isEqualTo(1L);
    assertThat(response.getMessage()).isEqualTo(StartHandler.EXISTS_RESPONSE);
  }
}

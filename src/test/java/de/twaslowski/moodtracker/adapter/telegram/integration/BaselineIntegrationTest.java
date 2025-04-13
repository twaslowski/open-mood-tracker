package de.twaslowski.moodtracker.adapter.telegram.integration;

import static org.assertj.core.api.Assertions.assertThat;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.IntegrationTestBase;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.command.AutoBaselineHandler;
import de.twaslowski.moodtracker.entity.UserSpec;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class BaselineIntegrationTest extends IntegrationTestBase {

  @Autowired private AutoBaselineHandler autoBaselineHandler;

  @Test
  void shouldToggleAutoBaseline() {
    initializeUser(UserSpec.valid().build());

    var update = TelegramTextUpdate.builder()
        .text(AutoBaselineHandler.COMMAND)
        .chatId(1).build();

    // User is created with autoBaseline = false; toggle to true
    autoBaselineHandler.handleUpdate(update);

    assertThat(userService.findAutoBaselineEligibleUsers()).isNotEmpty();

    autoBaselineHandler.handleUpdate(update);

    assertThat(userService.findAutoBaselineEligibleUsers()).isEmpty();
  }
}

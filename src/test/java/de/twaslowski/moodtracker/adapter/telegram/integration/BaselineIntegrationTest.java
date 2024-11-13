package de.twaslowski.moodtracker.adapter.telegram.integration;

import static org.assertj.core.api.Assertions.assertThat;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.domain.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.command.AutoBaselineHandler;
import de.twaslowski.moodtracker.entity.ConfigurationSpec;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.config.defaults.MoodMetric;
import de.twaslowski.moodtracker.config.defaults.SleepMetric;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class BaselineIntegrationTest extends IntegrationBase {

  @Autowired private AutoBaselineHandler autoBaselineHandler;

  @Test
  void shouldCreateAutoBaselineOnlyForEligibleUsers() {
    var invalidConfiguration1 = ConfigurationSpec.valid()
        .baselineMetrics(List.of())
        .build();

    var invalidConfiguration2 = ConfigurationSpec.withoutBaselineConfiguration()
        .autoBaselineEnabled(false)
        .build();

    var ineligibleUser1 = UserSpec.valid()
        .telegramId(2)
        .build();

    var ineligibleUser2 = UserSpec.valid()
        .telegramId(3)
        .build();

    saveUserWithDefaultConfiguration(UserSpec.valid().build());
    saveUserWithConfiguration(ineligibleUser1, invalidConfiguration1);
    saveUserWithConfiguration(ineligibleUser2, invalidConfiguration2);

    autoBaselineService.createAutoBaselines();

    assertThat(recordRepository.findAll()).hasSize(1);
    var record = recordRepository.findAll().getFirst();

    assertThat(record.getValues()).isEqualTo(List.of(MoodMetric.INSTANCE.defaultDatapoint(), SleepMetric.INSTANCE.defaultDatapoint()));
    assertThat(record.getUserId()).isEqualTo(userRepository.findByTelegramId(1L).get().getId());

    assertMessageWithExactTextSent(messageUtil.getMessage("notification.baseline.created"));
  }

  @Test
  void shouldToggleAutoBaseline() {
    saveUserWithDefaultConfiguration(UserSpec.valid().build());

    var update = TelegramTextUpdate.builder()
        .text(AutoBaselineHandler.COMMAND)
        .chatId(1).build();

    autoBaselineHandler.handleUpdate(update);

    assertThat(userService.findAutoBaselineEligibleUsers()).isEmpty();

    autoBaselineHandler.handleUpdate(update);

    assertThat(userService.findAutoBaselineEligibleUsers()).isNotEmpty();
  }
}

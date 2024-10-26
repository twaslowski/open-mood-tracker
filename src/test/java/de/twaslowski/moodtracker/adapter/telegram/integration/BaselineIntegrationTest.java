package de.twaslowski.moodtracker.adapter.telegram.integration;

import static org.assertj.core.api.Assertions.assertThat;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramTextUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.command.AutoBaselineHandler;
import de.twaslowski.moodtracker.entity.ConfigurationSpec;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
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
    var eligibleUser = UserSpec.valid().build();

    // Auto-baseline active, but no baseline configuration
    var ineligibleUser = UserSpec.valid()
        .id(2)
        .telegramId(2)
        .configuration(
            ConfigurationSpec.valid()
                .baselineMetrics(List.of())
                .autoBaselineEnabled(true)
                .build())
        .build();

    // Auto-baseline inactive
    var ineligibleUser2 = UserSpec.valid()
        .id(3)
        .telegramId(3)
        .configuration(
            ConfigurationSpec.valid()
                .autoBaselineEnabled(false)
                .build())
        .build();

    userRepository.save(eligibleUser);
    userRepository.save(ineligibleUser);
    userRepository.save(ineligibleUser2);

    autoBaselineService.createAutoBaselines();

    assertThat(recordRepository.findAll()).hasSize(1);
    var record = recordRepository.findAll().getFirst();

    assertThat(record.getValues()).isEqualTo(List.of(MetricDatapoint.defaultForMetric(Mood.INSTANCE)));
    assertThat(record.getUserId()).isEqualTo(userRepository.findByTelegramId(1L).get().getId());

    assertMessageWithTextSent(messageUtil.getMessage("notification.baseline.created"));
  }

  @Test
  void shouldToggleAutoBaseline() {
    givenUser(UserSpec.valid().build());

    var update = TelegramTextUpdate.builder()
        .text(AutoBaselineHandler.COMMAND)
        .chatId(1).build();

    autoBaselineHandler.handleUpdate(update);

    var user = userRepository.findByTelegramId(1L);
    assertThat(user).isPresent();
    assertThat(user.get().getConfiguration().isAutoBaselineEnabled()).isFalse();

    autoBaselineHandler.handleUpdate(update);

    user = userRepository.findByTelegramId(1L);
    assertThat(user).isPresent();
    assertThat(user.get().getConfiguration().isAutoBaselineEnabled()).isTrue();
  }
}

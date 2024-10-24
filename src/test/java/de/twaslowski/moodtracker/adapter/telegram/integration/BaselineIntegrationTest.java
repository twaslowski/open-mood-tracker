package de.twaslowski.moodtracker.adapter.telegram.integration;

import static org.assertj.core.api.Assertions.assertThat;

import de.twaslowski.moodtracker.Annotation.IntegrationTest;
import de.twaslowski.moodtracker.entity.ConfigurationSpec;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class BaselineIntegrationTest extends IntegrationBase {

  @Test
  void shouldCreateAutoBaselineOnlyForEligibleUsers() {
    var eligibleUser = UserSpec.valid().build();

    // Auto-baseline active, but no baseline configuration
    var ineligibleUser = UserSpec.valid()
        .telegramId(2)
        .configuration(
            ConfigurationSpec.valid()
                .baselineConfiguration(Set.of())
                .autoBaselineEnabled(true)
                .build())
        .build();

    // Auto-baseline inactive
    var ineligibleUser2 = UserSpec.valid()
        .telegramId(3)
        .configuration(
            ConfigurationSpec.valid()
                .autoBaselineEnabled(false)
                .build())
        .build();

    givenUser(eligibleUser);
    givenUser(ineligibleUser);
    givenUser(ineligibleUser2);

    autoBaselineService.createAutoBaselines();

    assertThat(recordRepository.findAll()).hasSize(1);
    var record = recordRepository.findAll().getFirst();

    assertThat(record.getValues()).isEqualTo(Set.of(MetricDatapoint.fromMetricDefault(new Mood())));
    assertThat(record.getTelegramId()).isEqualTo(eligibleUser.getId());

    assertMessageWithTextSent(messageUtil.getMessage("notification.baseline.created"));
  }
}

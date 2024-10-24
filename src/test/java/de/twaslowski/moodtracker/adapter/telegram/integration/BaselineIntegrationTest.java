package de.twaslowski.moodtracker.adapter.telegram.integration;

import static org.assertj.core.api.Assertions.assertThat;

import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class BaselineIntegrationTest extends IntegrationBase {

  @Test
  void shouldCreateAutoBaselineOnlyForEligibleUsers() {
    var eligibleUser = UserSpec.valid()
        .baselineConfiguration(Set.of(MetricDatapoint.fromMetricDefault(new Mood())))
        .autoBaselineEnabled(true).build();

    var ineligibleUser = UserSpec.valid()
        .telegramId(2)
        .baselineConfiguration(null)
        .autoBaselineEnabled(true).build();

    var ineligibleUser2 = UserSpec.valid()
        .telegramId(3)
        .baselineConfiguration(Set.of(MetricDatapoint.fromMetricDefault(new Mood())))
        .autoBaselineEnabled(false).build();

    userRepository.save(eligibleUser);
    userRepository.save(ineligibleUser);
    userRepository.save(ineligibleUser2);

    autoBaselineService.createAutoBaselines();

    assertThat(recordRepository.findAll()).hasSize(1);
    var record = recordRepository.findAll().getFirst();

    assertThat(record.getValues()).isEqualTo(Set.of(MetricDatapoint.fromMetricDefault(new Mood())));
    assertThat(record.getTelegramId()).isEqualTo(eligibleUser.getId());
  }
}

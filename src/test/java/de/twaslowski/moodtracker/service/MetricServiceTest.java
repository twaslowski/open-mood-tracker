package de.twaslowski.moodtracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.domain.dto.MetricDTO;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.exception.MetricNotFoundException;
import de.twaslowski.moodtracker.repository.MetricRepository;
import de.twaslowski.moodtracker.repository.MetricConfigurationRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetricServiceTest {

  @Mock
  private MetricRepository metricRepository;

  @Mock
  private MetricConfigurationRepository metricConfigurationRepository;

  @InjectMocks
  private MetricService metricService;

  @Test
  void shouldThrowExceptionWhenNonExistentMetricIsUpdated() {
    var user = UserSpec.valid().build();
    when(metricRepository.findById(1L)).thenReturn(Optional.empty());

    var dto = MetricDTO.builder().id(1L).build();

    assertThatThrownBy(() -> metricService.updateMetricConfiguration(user, dto))
        .isInstanceOf(MetricNotFoundException.class);
  }

  @Test
  void shouldCreateMetricConfigurationIfNotExists() {
    var user = UserSpec.valid().build();
    var existingMetric = Metric.builder().id(1L).defaultValue(1).build();
    var dto = MetricDTO.builder().id(1L).baseline(2).build();

    when(metricRepository.findById(1L)).thenReturn(Optional.of(existingMetric));
    when(metricConfigurationRepository.findByUserIdAndMetricId(user.getId(), existingMetric.getId()))
        .thenReturn(Optional.empty());
    when(metricConfigurationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = metricService.updateMetricConfiguration(user, dto);

    assertThat(result.getMetric()).isEqualTo(existingMetric);
    assertThat(result.getUser()).isEqualTo(user);
    assertThat(result.getBaselineValue()).isEqualTo(2);
  }

  @Test
  void shouldUpdateMetricConfiguration() {
    var user = UserSpec.valid().build();
    var existingMetric = Metric.builder().id(1L).defaultValue(1).build();
    var metricConfiguration = MetricConfiguration.from(existingMetric, user);
    var dto = MetricDTO.builder().id(1L).baseline(2).build();

    when(metricRepository.findById(1L)).thenReturn(Optional.of(existingMetric));
    when(metricConfigurationRepository.findByUserIdAndMetricId(user.getId(), existingMetric.getId()))
        .thenReturn(Optional.of(metricConfiguration));
    when(metricConfigurationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = metricService.updateMetricConfiguration(user, dto);

    assertThat(result.getMetric()).isEqualTo(existingMetric);
    assertThat(result.getUser()).isEqualTo(user);
    assertThat(result.getBaselineValue()).isEqualTo(2);
  }

}
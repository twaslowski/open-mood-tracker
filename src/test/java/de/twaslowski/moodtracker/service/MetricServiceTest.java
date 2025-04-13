package de.twaslowski.moodtracker.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.domain.dto.MetricDTO;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.entity.UserSpec;
import de.twaslowski.moodtracker.exception.MetricNotFoundException;
import de.twaslowski.moodtracker.exception.MetricOwnerMismatchException;
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

    assertThatThrownBy(() -> metricService.updateMetric(user, dto))
        .isInstanceOf(MetricNotFoundException.class);
  }

  @Test
  void shouldCloneDefaultMetricWhenUpdatingDefaultMetric() {
    var user = UserSpec.valid().build();
    var existingMetric = Metric.builder().id(1L).defaultMetric(true).build();
    var dto = MetricDTO.builder().id(1L).build();

    when(metricRepository.findById(1L)).thenReturn(Optional.of(existingMetric));
    when(metricRepository.save(any(Metric.class))).thenAnswer(invocation -> invocation.getArgument(0));

    var result = metricService.updateMetric(user, dto);

    assertNotNull(result);
    assertEquals(user.getId(), result.getOwnerId());
  }

  @Test
  void shouldThrowExceptionWhenUserDoesNotOwnMetric() {
    var user = UserSpec.valid().build();
    var existingMetric = Metric.builder().id(1L).defaultMetric(false).ownerId("otherUserId").build();
    var dto = MetricDTO.builder().id(1L).build();

    when(metricRepository.findById(1L)).thenReturn(Optional.of(existingMetric));

    assertThatThrownBy(() -> metricService.updateMetric(user, dto))
        .isInstanceOf(MetricOwnerMismatchException.class);
  }

  @Test
  void shouldUpdateNonDefaultMetricSuccessfully() {
    var user = UserSpec.valid().build();
    var existingMetric = Metric.builder().id(1L).defaultMetric(false).ownerId(user.getId()).build();
    var dto = mock(MetricDTO.class);

    when(dto.id()).thenReturn(existingMetric.getId());
    when(dto.validateLabels()).thenReturn(true);

    when(metricRepository.findById(1L)).thenReturn(Optional.of(existingMetric));
    when(metricRepository.save(any(Metric.class))).thenAnswer(invocation -> invocation.getArgument(0));

    var result = metricService.updateMetric(user, dto);

    assertNotNull(result);
    assertEquals(existingMetric.getId(), result.getId());
  }
}
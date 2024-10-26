package de.twaslowski.moodtracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.entity.Configuration;
import de.twaslowski.moodtracker.entity.Record;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import de.twaslowski.moodtracker.repository.RecordRepository;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecordServiceTest {

  @Mock
  private RecordRepository recordRepository;

  @Mock
  private UserService userService;

  @Mock
  private MetricService metricService;

  @InjectMocks
  private RecordService recordService;

  @Test
  void shouldReturnFirstIncompleteMetric() {
    // given
    var record = Record.builder()
        .values(List.of(MetricDatapoint.emptyForMetric(Mood.NAME)))
        .userId(1)
        .build();

    when(userService.getUserConfiguration(1L)).thenReturn(Configuration.builder()
        .metrics(List.of(Mood.NAME))
        .build());

    when(metricService.getMetricByName(Mood.NAME)).thenReturn(Mood.INSTANCE);

    // when
    var nextIncompleteMetric = recordService.getNextIncompleteMetric(record);

    // then
    assertThat(nextIncompleteMetric).isPresent();
    assertThat(nextIncompleteMetric.get().getName()).isEqualTo(Mood.NAME);
  }

  @Test
  void shouldReturnEmptyOptionalIfAllMetricsComplete() {
    // given
    var record = Record.builder()
        .userId(1L)
        .values(List.of(MetricDatapoint.forMetricWithValue(Mood.INSTANCE, 2)))
        .build();

    // when
    when(userService.getUserConfiguration(1L)).thenReturn(Configuration.builder()
        .metrics(List.of(Mood.NAME))
        .build());

    var nextIncompleteMetric = recordService.getNextIncompleteMetric(record);

    // then
    assertThat(nextIncompleteMetric).isEmpty();
  }

  @Test
  void shouldReturnNewerRecordIfMultipleIncompleteRecordsExist() {
    // given
    var record1 = Record.builder()
        .userId(1)
        .creationTimestamp(ZonedDateTime.now().minusHours(1))
        .values(List.of(MetricDatapoint.emptyForMetric(Mood.NAME)))
        .build();

    var record2 = Record.builder()
        .userId(1)
        .creationTimestamp(ZonedDateTime.now())
        .values(List.of(MetricDatapoint.emptyForMetric(Sleep.NAME)))
        .build();

    when(recordRepository.findByUserId(1)).thenReturn(List.of(record1, record2));

    // when
    var incompleteRecord = recordService.findIncompleteRecordsForUser(1);

    // then
    assertThat(incompleteRecord).isPresent();
    assertThat(incompleteRecord.get()).isEqualTo(record2);
  }
}

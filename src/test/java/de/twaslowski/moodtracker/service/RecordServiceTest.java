package de.twaslowski.moodtracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.domain.entity.Record;
import de.twaslowski.moodtracker.config.defaults.MoodMetric;
import de.twaslowski.moodtracker.config.defaults.SleepMetric;
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
        .values(List.of(MoodMetric.INSTANCE.emptyDatapoint()))
        .userId(1)
        .build();

    when(metricService.getMetricById(1L)).thenReturn(MoodMetric.INSTANCE);

    // when
    var nextIncompleteMetric = recordService.getNextIncompleteMetric(record);

    // then
    assertThat(nextIncompleteMetric).isPresent();
    assertThat(nextIncompleteMetric.get().getName()).isEqualTo(MoodMetric.NAME);
  }

  @Test
  void shouldReturnEmptyOptionalIfAllMetricsComplete() {
    // given
    var record = Record.builder()
        .userId(1L)
        .values(List.of(MoodMetric.INSTANCE.datapointWithValue(2)))
        .build();

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
        .values(List.of(MoodMetric.INSTANCE.emptyDatapoint()))
        .build();

    var record2 = Record.builder()
        .userId(1)
        .creationTimestamp(ZonedDateTime.now())
        .values(List.of(SleepMetric.INSTANCE.emptyDatapoint()))
        .build();

    when(recordRepository.findByUserId(1)).thenReturn(List.of(record1, record2));

    // when
    var incompleteRecord = recordService.findIncompleteRecordsForUser(1);

    // then
    assertThat(incompleteRecord).isPresent();
    assertThat(incompleteRecord.get()).isEqualTo(record2);
  }

  @Test
  void shouldStringifyRecord() {
    // given
    var record = Record.builder()
        .userId(1)
        .values(List.of(
            SleepMetric.INSTANCE.datapointWithValue(5)
        ))
        .build();

    when(metricService.getMetricById(2)).thenReturn(SleepMetric.INSTANCE);

    // when
    var stringifiedRecord = recordService.stringifyRecord(record);

    // then
    assertThat(stringifiedRecord).isEqualToIgnoringCase("Sleep: 5\n");
  }
}

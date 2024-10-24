package de.twaslowski.moodtracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.twaslowski.moodtracker.config.MetricsConfiguration;
import de.twaslowski.moodtracker.entity.Record;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.entity.metric.Mood;
import de.twaslowski.moodtracker.entity.metric.Sleep;
import de.twaslowski.moodtracker.repository.RecordRepository;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecordServiceTest {

  private final LinkedHashMap<String, Metric> metrics = new MetricsConfiguration().defaultMetrics();
  private final RecordRepository recordRepository = mock(RecordRepository.class);

  private final RecordService recordService = new RecordService(recordRepository, metrics);

  @Test
  void shouldReturnFirstIncompleteMetric() {
    // given
    var record = Record.builder()
        .values(Set.of(
                MetricDatapoint.forMetric(Mood.TYPE),
                MetricDatapoint.forMetric(Sleep.TYPE)
            )
        )
        .build();

    // when
    var nextIncompleteMetric = recordService.getNextIncompleteMetric(record);

    // then
    assertThat(nextIncompleteMetric).isPresent();
    assertThat(nextIncompleteMetric.get().getName()).isEqualTo(Mood.TYPE);
  }

  @Test
  void shouldReturnEmptyOptionalIfAllMetricsComplete() {
    // given
    var record = Record.builder()
        .values(Set.of())
        .build();

    // when
    var nextIncompleteMetric = recordService.getNextIncompleteMetric(record);

    // then
    assertThat(nextIncompleteMetric).isEmpty();
  }

  @Test
  void shouldReturnNewerRecordIfMultipleIncompleteRecordsExist() {
    // given
    var record1 = Record.builder()
        .telegramId(1)
        .creationTimestamp(ZonedDateTime.now().minusHours(1))
        .values(Set.of(MetricDatapoint.forMetric(Mood.TYPE)))
        .build();
    var record2 = Record.builder()
        .telegramId(1)
        .creationTimestamp(ZonedDateTime.now())
        .values(Set.of(MetricDatapoint.forMetric(Sleep.TYPE)))
        .build();

    when(recordRepository.findByTelegramId(1)).thenReturn(List.of(record1, record2));

    // when
    var incompleteRecord = recordService.findIncompleteRecordForTelegramChat(1);

    // then
    assertThat(incompleteRecord).isPresent();
    assertThat(incompleteRecord.get()).isEqualTo(record2);
  }
}

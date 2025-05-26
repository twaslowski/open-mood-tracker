package de.twaslowski.moodtracker.service;

import de.twaslowski.moodtracker.domain.dto.DatapointDTO;
import de.twaslowski.moodtracker.domain.dto.RecordDTO;
import de.twaslowski.moodtracker.domain.entity.Metric;
import de.twaslowski.moodtracker.domain.entity.MetricConfiguration;
import de.twaslowski.moodtracker.domain.entity.Record;
import de.twaslowski.moodtracker.domain.entity.Record.Status;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.domain.value.MetricDatapoint;
import de.twaslowski.moodtracker.repository.RecordRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordService {

  private final RecordRepository recordRepository;
  private final MetricService metricService;
  private final UserService userService;

  public Record initializeFrom(User user) {
    List<MetricConfiguration> metricConfigurations = userService.getTrackedMetrics(user);
    if (metricConfigurations.isEmpty()) {
      log.warn("No tracked metrics found for user: {}", user.getId());
    }

    List<MetricDatapoint> initialDatapoints = metricConfigurations.stream()
        .map(MetricConfiguration::emptyDatapoint)
        .collect(Collectors.toList());

    var record = Record.builder()
        .status(Status.IN_PROGRESS)
        .userId(user.getId())
        .values(initialDatapoints)
        .build();

    return recordRepository.save(record);
  }

  public List<RecordDTO> getRecords(String userId) {
    return recordRepository.findByUserId(userId).stream()
        .filter(Record::completed)
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  public RecordDTO toDTO(Record record) {
    return RecordDTO.builder()
        .timestamp(record.getCreationTimestamp())
        .datapoints(record.getValues().stream()
            .map(this::toDatapointDTO)
            .collect(Collectors.toList()))
        .build();
  }

  public DatapointDTO toDatapointDTO(MetricDatapoint metricDatapoint) {
    var metric = metricService.getMetricById(metricDatapoint.metricId());
    return DatapointDTO.builder()
        .metricId(metric.getId())
        .metricName(metric.getName())
        .metricDescription(metric.getDescription())
        .minValue(metric.getMinValue())
        .maxValue(metric.getMaxValue())
        .datapointValue(metricDatapoint.value())
        .labels(metric.getLabels())
        .build();
  }

  public void recordFromBaseline(User user) {
    var baselineConfiguration = userService.getBaselineConfiguration(user.getId());
    recordRepository.save(
        Record.builder()
            .userId(user.getId())
            .status(Status.COMPLETED)
            .values(baselineConfiguration)
            .build()
    );
  }

  public boolean userRecordedToday(User user) {
    return recordRepository.findCompleteRecordByUserAndDate(user.getId(), LocalDate.now()).isEmpty();
  }

  public Optional<Record> findIncompleteRecordsForUser(String userId) {
    var incompleteRecords = recordRepository.findByUserId(userId).stream()
        .filter(Record::inProgress)
        .collect(Collectors.toSet());

    if (incompleteRecords.size() > 1) {
      log.warn("Found multiple incomplete records for chatId: {}", userId);
    }

    return incompleteRecords.stream()
        .max(Comparator.comparing(Record::getCreationTimestamp));
  }

  public Optional<Metric> getNextIncompleteMetric(Record record) {
    return record.getIncompleteMetricIds().stream()
        .map(metricService::getMetricById)
        .findFirst();
  }

  public String stringifyRecord(Record record) {
    var result = new StringBuilder();
    for (MetricDatapoint datapoint : record.getValues()) {
      var metric = metricService.getMetricById(datapoint.metricId());
      result.append(metric.getName());
      result.append(": ");
      result.append(metric.getLabelFor(datapoint.value()).label());
      result.append("\n");
    }
    return result.toString();
  }
}

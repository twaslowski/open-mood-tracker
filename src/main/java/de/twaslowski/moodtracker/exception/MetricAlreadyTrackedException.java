package de.twaslowski.moodtracker.exception;

import static java.lang.String.format;

public class MetricAlreadyTrackedException extends RuntimeException {

  public MetricAlreadyTrackedException(String userId, long metricId) {
    super(format("User %s is already tracking metric %d", userId, metricId));
  }
}

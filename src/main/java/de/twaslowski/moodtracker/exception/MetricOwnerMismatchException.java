package de.twaslowski.moodtracker.exception;

import static java.lang.String.format;

public class MetricOwnerMismatchException extends RuntimeException {

  public MetricOwnerMismatchException(String userId, long metricId) {
    super(format("User %s does not own metric %d", userId, metricId));
  }
}

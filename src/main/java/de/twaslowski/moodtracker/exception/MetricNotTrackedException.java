package de.twaslowski.moodtracker.exception;

import static java.lang.String.format;

public class MetricNotTrackedException extends RuntimeException {

  public MetricNotTrackedException(String userId, String trackedMetricId) {
    super(format("Tracked metric %s does not exist for user %s", trackedMetricId, userId));
  }
}

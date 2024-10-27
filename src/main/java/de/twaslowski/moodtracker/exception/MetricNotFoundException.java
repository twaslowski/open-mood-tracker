package de.twaslowski.moodtracker.exception;

public class MetricNotFoundException extends IllegalArgumentException {

  public MetricNotFoundException(String message) {
    super(message);
  }
}

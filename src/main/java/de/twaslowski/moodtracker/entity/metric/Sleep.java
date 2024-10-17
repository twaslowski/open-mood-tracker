package de.twaslowski.moodtracker.entity.metric;

public class Sleep extends Metric {

  public static final String NAME = "SLEEP";
  private static final int LOWER_BOUND = -3;
  private static final int UPPER_BOUND = 3;

  public Sleep() {
    super(NAME, LOWER_BOUND, UPPER_BOUND, null);
  }

  public Sleep(int value) {
    super(NAME, LOWER_BOUND, UPPER_BOUND, value);
  }

  public static Sleep empty() {
    return new Sleep();
  }

  public static Sleep of(int value) {
    return new Sleep(value);
  }
}

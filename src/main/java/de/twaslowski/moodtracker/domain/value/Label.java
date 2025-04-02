package de.twaslowski.moodtracker.domain.value;

public record Label(
    String label,
    Integer value
) {

  public static Label ofInt(Integer value) {
    return new Label(String.valueOf(value), value);
  }

  public static Label of(Integer value, String label) {
    return new Label(label, value);
  }
}

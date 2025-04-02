package de.twaslowski.moodtracker.domain.value;

import java.util.Comparator;

public class LabelComparator implements Comparator<Label> {

  @Override
  public int compare(Label label1, Label label2) {
    return label1.value().compareTo(label2.value());
  }
}

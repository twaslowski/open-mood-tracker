package de.twaslowski.moodtracker.adapter.telegram.external;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InMemoryQueue<T> {

  private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

  public void add(T item) {
    queue.add(item);
  }

  public T take() throws InterruptedException {
    return queue.take();
  }
}
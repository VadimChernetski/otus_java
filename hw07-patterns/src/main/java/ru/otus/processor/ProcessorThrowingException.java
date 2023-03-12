package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalTime;

public class ProcessorThrowingException implements Processor {

  @Override
  public Message process(Message message) {
    final LocalTime now = LocalTime.now();
    if (now.getSecond() % 2 == 0) {
      throw new RuntimeException("ProcessorThrowingException");
    } else {
      try {
        Thread.sleep(1000);
        throw new RuntimeException("ProcessorThrowingException");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}

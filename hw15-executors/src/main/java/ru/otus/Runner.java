package ru.otus;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Runner {
  static Lock lock = new ReentrantLock();
  static Condition condition = lock.newCondition();

  public static void main(String[] args) {
    Thread first = new Thread(Runner::print);
    Thread second = new Thread(Runner::print);
    first.start();
    second.start();
  }

  private static void print()  {
    int index = 1;
    try {
      while (index <= 10) {
        lock.lock();
        condition.await(2, TimeUnit.SECONDS);
        System.out.println(Thread.currentThread().getName() + ": " + index++);
        condition.signal();
        lock.unlock();
      }
      index--;
      while (index > 0) {
        lock.lock();
        condition.await(2, TimeUnit.SECONDS);
        System.out.println(Thread.currentThread().getName() + ": " + --index);
        condition.signal();
        lock.unlock();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}


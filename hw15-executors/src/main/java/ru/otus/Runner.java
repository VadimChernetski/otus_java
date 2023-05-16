package ru.otus;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Runner {

  private static Object monitor = new Object();

  private static String currentThread = "Second";

  public static void main(String[] args) {
    Thread first = new Thread(() -> print("Second"));
    first.setName("First");
    Thread second = new Thread(() -> print("First"));
    second.setName("Second");
    second.start();
    first.start();
  }

  private static void print(String nextThreadName)  {
    int index = 1;
      while (index <= 10) {
        synchronized (monitor) {
          try {
            while (!Thread.currentThread().getName().equals(currentThread)) {
              monitor.wait();
            }
            System.out.println(Thread.currentThread().getName() + ": " + index++);
            currentThread = nextThreadName;
            monitor.notifyAll();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
      }
      index = 9;
      while (index > 0) {
        synchronized (monitor) {
          try {
            while (!Thread.currentThread().getName().equals(currentThread)) {
              monitor.wait();
            }
            System.out.println(Thread.currentThread().getName() + ": " + index--);
            currentThread = nextThreadName;
            monitor.notifyAll();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
      }
  }
}


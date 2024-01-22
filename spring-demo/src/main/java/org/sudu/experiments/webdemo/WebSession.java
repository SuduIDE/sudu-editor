package org.sudu.experiments.webdemo;

import org.sudu.experiments.parser.activity.ActivitySession;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WebSession {
  final ReadWriteLock lock = new ReentrantReadWriteLock(true);
  final ActivitySession activity = new ActivitySession();

  void setSource(String src) {
    Lock writeLock = lock.writeLock();
    writeLock.lock();
    try {
      activity.setSource(src);
    } finally {
      writeLock.unlock();
    }
  }

  void setSeed(int seed) {
    Lock writeLock = lock.writeLock();
    writeLock.lock();
    try {
      activity.setSeed(seed);
    } finally {
      writeLock.unlock();
    }
  }

  String source() {
    Lock readLock = lock.readLock();
    readLock.lock();
    try {
      return activity.source();
    } finally {
      readLock.unlock();
    }
  }

  String dag1() {
    Lock readLock = lock.readLock();
    readLock.lock();
    try {
      return activity.dag1();
    } finally {
      readLock.unlock();
    }
  }

  // dag2() writes to activity
  String dag2() {
    Lock writeLock = lock.writeLock();
    writeLock.lock();
    try {
      return activity.dag2();
    } finally {
      writeLock.unlock();
    }
  }

  String calculatePaths() {
    Lock writeLock = lock.writeLock();
    writeLock.lock();
    try {
      return activity.calculatePaths();
    } finally {
      writeLock.unlock();
    }
  }

  public String highlight(int groupIndex, int index) {
    Lock writeLock = lock.writeLock();
    writeLock.lock();
    try {
      return activity.highlight(groupIndex, index);
    } finally {
      writeLock.unlock();
    }
  }
}

package com.port.model;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {
  private static final AtomicReference<Port> instance = new AtomicReference<>();

  private final Lock lock = new ReentrantLock(true);
  private final Condition berthAvailable = lock.newCondition();

  private int berths;
  private int warehouseContainers;
  private final int warehouseCapacity;

  private Port(int berths, int warehouseContainers, int warehouseCapacity) {
    this.berths = berths;
    this.warehouseContainers = warehouseContainers;
    this.warehouseCapacity = warehouseCapacity;
  }

  public static Port getInstance(int berths, int warehouseContainers, int warehouseCapacity) {
    Port port = instance.get();
    if (port == null) {
      port = new Port(berths, warehouseContainers, warehouseCapacity);
      if(!instance.compareAndSet(null, port)) {
        port = instance.get();
      }
    }
    return port;
  }

  public static Port getInstance() {
    Port port = instance.get();
    if(port ==null) {
      throw new IllegalStateException("Needed to inicialize port");
    }
    return port;
  }

   public void lockBerth() throws InterruptedException {
    lock.lock();
    try {
      while (berths <= 0) {
        berthAvailable.await();
      }
      berths--;
    } finally {
      lock.unlock();
    }
  }

  public void unlockBerth() {
    lock.lock();
    try {
      berths++;
      berthAvailable.signal();
    } finally {
      lock.unlock();
    }
  }

  public int getWarehouseContainers() {
    lock.lock();
    try{
      return warehouseContainers;
    } finally { lock.unlock(); }
  }

  public int getWarehouseCapacity() {
    return warehouseCapacity;
  }

  public int unloadContainers(int count) {
    if (count <= 0) { return 0; };

    lock.lock();
    try {
      int freeSpace = warehouseCapacity - warehouseContainers;
      if (freeSpace <= 0) { return 0; }

      int canUnload = Math.min(count, freeSpace);
      warehouseContainers += canUnload;
      
      return canUnload;
    } finally { lock.unlock(); }
  }

  public int loadContainers(int count) {
    if (count <= 0) { return 0; };

    lock.lock();
    try {
      if (warehouseContainers <= 0) { return 0; }

      int canLoad = Math.min(count, warehouseContainers);
      warehouseContainers -= canLoad;
      
      return canLoad;
    } finally { lock.unlock(); }
  }
}

package com.port.model;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Port {
  private static final AtomicReference<Port> instance = new AtomicReference<>();
  
  private final Semaphore berths;
  private final AtomicInteger warehouseContainers;
  private final int warehouseCapacity;

  private Port(int berths, int warehouseContainers, int warehouseCapacity) {
    this.berths = new Semaphore(berths, true);
    this.warehouseContainers = new AtomicInteger(warehouseContainers);
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

  public Semaphore getBerths() {
    return berths;
  }

  public AtomicInteger getWarehouseContainers() {
    return warehouseContainers;
  }

  public int getWarehouseCapacity() {
    return warehouseCapacity;
  }

  public int addContainers(int containersToUnload) {
    if (containersToUnload <= 0) { return 0; }

    while (true) {
      int current = warehouseContainers.get();
      int freeSpace = warehouseCapacity - current;

      if (freeSpace <= 0) { return containersToUnload; }

      int canTake = Math.min(containersToUnload, freeSpace);
      int nextValue = current + canTake;

      if (warehouseContainers.compareAndSet(current, nextValue)) {
        return canTake; 
      }
    }
  }

  public int getContainers(int containersToLoad) {
    if (containersToLoad <= 0) { return 0; }

    while (true) {
      int current = warehouseContainers.get();

      if (current <= 0) { return 0; }
      int canLoad = Math.min(containersToLoad, current);
      int nextValue = current - canLoad;

      if (warehouseContainers.compareAndSet(current, nextValue)) {
        return canLoad; 
      }
    }
  }
}

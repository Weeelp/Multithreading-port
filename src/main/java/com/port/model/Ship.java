package com.port.model;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.port.exception.ProjectException;
import com.port.state.ShipState;
import com.port.state.impl.CompletedState;
import com.port.state.impl.WaitingState;

public class Ship implements Callable<String> {
  private static final Logger Log = LogManager.getLogger();

  private final int shipId;
  private final AtomicInteger currentContainers;
  private final int shipCapacity;
  private final boolean needsUnload;
  private final boolean needsLoad;
  private ShipState state = new WaitingState(); 
    
  public Ship (int shipId,
    int currentContainers,
    int shipCapacity,
    boolean needsUnload,
    boolean needsLoad
  ) {
    this.shipId = shipId;
    this.currentContainers = new AtomicInteger(currentContainers);
    this.shipCapacity = shipCapacity;
    this.needsUnload = needsUnload;
    this.needsLoad = needsLoad;
  }
  @Override
  public String call() throws Exception {
    Port port = Port.getInstance();
    Log.info("Ship {} near the port.", shipId);

    try {
      port.lockBerth();
      try{
      Log.info("Ship {} near the berth.", shipId);

      while(!(this.state instanceof CompletedState)) {
        state.next(this);
        state.printStatus(this);
        state.processAction(this, port);
      }
    } finally {
      Log.info("Ship {} leave the berth.", shipId);
      port.unlockBerth();
    }
    } catch (InterruptedException e) {
      throw new ProjectException("Ship " + shipId + " was interrupted during processing.",e);
    }
    return "Ship " + shipId + " processed succesfully";  
  }

  public int getCountsOfCurrentContainers() {return currentContainers.get(); }
  public int getCapacity() {return shipCapacity;}
  public int getShipId() { return shipId; }

  public boolean isNeedsUnload() { return needsUnload; }
  public boolean isNeedsLoad() { return needsLoad; }

  public void setState(ShipState state) { this.state = state; }
  public void setCountOfCurrentContainers(int value) { this.currentContainers.addAndGet(value); }
  
}

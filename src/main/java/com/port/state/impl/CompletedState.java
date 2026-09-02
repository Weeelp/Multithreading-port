package com.port.state.impl;

import com.port.model.Port;
import com.port.model.Ship;
import com.port.state.ShipState;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompletedState implements ShipState{
  private static final Logger Log = LogManager.getLogger();

 @Override
  public void next(Ship ship) {
    Log.warn("Ship {} is already in the final state.", ship.getShipId());
  }

  @Override
  public void printStatus(Ship ship) {
    Log.info("Ship {}: Status changed to [Completed]. Leaving the port", ship.getShipId());
  }

  @Override
  public void processAction(Ship ship, Port port) throws InterruptedException {}
   
}

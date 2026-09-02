package com.port.state.impl;

import com.port.model.Port;
import com.port.model.Ship;
import com.port.state.ShipState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitingState implements ShipState {
  private static final Logger Log = LogManager.getLogger();

  @Override
  public void next(Ship ship) {
    if(ship.isNeedsUnload()) {
      ship.setState(new UnloadingState());
    } else if(ship.isNeedsLoad()) {
      ship.setState(new LoadingState());
    } else {
      ship.setState(new CompletedState());
    }
  }

  @Override
  public void printStatus(Ship ship) {
    Log.info("Ship {}: Status changed to [Waiting].", ship.getShipId());
  }

  @Override
  public void processAction(Ship ship, Port port) throws InterruptedException {}
  
}

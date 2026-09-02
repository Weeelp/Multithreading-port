package com.port.state.impl;

import com.port.model.Port;
import com.port.model.Ship;
import com.port.state.ShipState;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnloadingState implements ShipState {
  private static final Logger Log = LogManager.getLogger();
  
  @Override
  public void next(Ship ship) {
    if(ship.isNeedsLoad()) {
      ship.setState(new LoadingState());
    } else {
      ship.setState(new CompletedState());
    }
  }

  @Override
  public void printStatus(Ship ship) {
    Log.info("Ship {}: Status changed to [Unloading].", ship.getShipId());
  }

  @Override
  public void processAction(Ship ship, Port port) throws InterruptedException {
    int containersToUnload = ship.getCountsOfCurrentContainers();

    if(containersToUnload > 0) {
      int unloadedCount = port.addContainers(containersToUnload);
      ship.setCountOfCurrentContainers(-unloadedCount);

      TimeUnit.SECONDS.sleep(3);
      Log.info("Ship {} unloaded {} containers to the port. Left on ship: {}", 
            ship.getShipId(), unloadedCount, ship.getCountsOfCurrentContainers());
            
        if (unloadedCount < containersToUnload) {
            Log.warn("Port is full! Ship {} couldn't unload all containers.", ship.getShipId());
        }
    }
  }

  
    
}

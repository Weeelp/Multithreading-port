package com.port.state.impl;

import com.port.model.Port;
import com.port.model.Ship;
import com.port.state.ShipState;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadingState implements ShipState{
  private static final Logger Log = LogManager.getLogger();
@Override
  public void next(Ship ship) {
    ship.setState(new CompletedState());
  }

  @Override
  public void printStatus(Ship ship) {
    Log.info("Ship {}: Status changed to [Loading].", ship.getShipId());
  }

  @Override
  public void processAction(Ship ship, Port port) throws InterruptedException {
    int containersToLoad = ship.getCountsOfCurrentContainers();

    if(containersToLoad > 0) {
      int loadCount = port.getContainers(containersToLoad);
      ship.setCountOfCurrentContainers(loadCount);

      TimeUnit.SECONDS.sleep(3);
      Log.info("Ship {} loadCount {} containers from the port. Left on ship: {}", 
            ship.getShipId(), loadCount, ship.getCountsOfCurrentContainers());
    }
  }
  
  
}

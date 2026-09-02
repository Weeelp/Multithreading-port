package com.port.state;

import com.port.model.Port;
import com.port.model.Ship;

public interface ShipState {
  void next(Ship ship);
  void printStatus(Ship ship);
  void processAction(Ship ship, Port port) throws InterruptedException;
}

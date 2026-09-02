package com.port.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.port.exception.ProjectException;
import com.port.model.Ship;
import com.port.parser.ShipParser;

public class ShipParserImpl implements ShipParser {

  @Override
  public List<Ship> parse(String inf) throws ProjectException {
    List<Ship> ships = new ArrayList<>();
    for (String line: inf.split("\n")) {
      if (line.strip().isBlank()) { continue; }
      String[] parts = line.split(",");
      int id = Integer.parseInt(parts[0].trim());
      int currentCargo = Integer.parseInt(parts[1].trim());
      int capacity = Integer.parseInt(parts[2].trim());
      boolean needsUnload = Boolean.parseBoolean(parts[3].trim());
      boolean needsLoad = Boolean.parseBoolean(parts[4].trim());
                
      ships.add(new Ship(id, currentCargo, capacity, needsUnload, needsLoad));
    }// TODO ship inf validation
    return ships;
  }
  
}

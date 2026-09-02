package com.port.parser;

import java.util.List;

import com.port.exception.ProjectException;
import com.port.model.Ship;

public interface ShipParser {
  public List<Ship> parse(String inf) throws ProjectException;
}

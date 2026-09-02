package com.port.reader.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.port.exception.ProjectException;
import com.port.reader.TextReader;

public class TextReaderImpl implements TextReader {
  
  private final String filePath;

  public TextReaderImpl (String filePath) {
    this.filePath = filePath;
  }

  @Override
  public String read() throws ProjectException {
    try {
      Path path = Paths.get(filePath);
      return Files.readString(path);
    } catch (IOException | InvalidPathException e) {
      throw new ProjectException("Failed to read file: " + filePath, e);
    }
  }
  
}

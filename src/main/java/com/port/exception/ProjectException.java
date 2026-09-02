package com.port.exception;

public class ProjectException extends Exception {
  public ProjectException (String e, Throwable couse) {
    super(e, couse);
  }

  public ProjectException (Throwable couse) {
    super(couse);
  }

  public ProjectException (String e) {
    super(e);
  }
}

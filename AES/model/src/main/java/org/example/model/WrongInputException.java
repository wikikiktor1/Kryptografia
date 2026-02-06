package org.example.model;

public class WrongInputException extends RuntimeException {
  public WrongInputException(String message) {
    super(message);
  }

  public WrongInputException(String message, Throwable cause) {
    super(message, cause);
  }
}

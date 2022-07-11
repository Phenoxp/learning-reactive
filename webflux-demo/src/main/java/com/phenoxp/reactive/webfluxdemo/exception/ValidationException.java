package com.phenoxp.reactive.webfluxdemo.exception;

public class ValidationException extends RuntimeException {

  private static final String MSG = "allowed range is 10 - 20";
  public static final int ERROR_CODE = 100;
  private final int input;

  public ValidationException() {
    super(MSG);
    this.input = 0;
  }

  public ValidationException(int input) {
    super(MSG);
    this.input = input;
  }

  public int getInput() {
    return input;
  }
}

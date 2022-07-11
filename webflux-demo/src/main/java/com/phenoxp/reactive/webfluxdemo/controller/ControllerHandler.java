package com.phenoxp.reactive.webfluxdemo.controller;

import com.phenoxp.reactive.webfluxdemo.exception.ValidationException;
import com.phenoxp.reactive.webfluxdemo.model.ErrorValidation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerHandler {

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorValidation> handleException(ValidationException exp) {
    var errorValidation =
        ErrorValidation.builder()
            .errorCode(ValidationException.ERROR_CODE)
            .input(exp.getInput())
            .message(exp.getMessage())
            .build();

    return ResponseEntity.badRequest().body(errorValidation);
  }
}

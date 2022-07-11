package com.phenoxp.reactive.webfluxdemo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class Response {

  private int output;
  private LocalDateTime date = LocalDateTime.now();

  public Response(int output) {
    this.output = output;
  }
}

package com.phenoxp.reactive.webfluxdemo.controller;

import com.phenoxp.reactive.webfluxdemo.exception.ValidationException;
import com.phenoxp.reactive.webfluxdemo.model.Request;
import com.phenoxp.reactive.webfluxdemo.model.Response;
import com.phenoxp.reactive.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathController {

  @Autowired
  private ReactiveMathService service;

  // Traditional way of exception handling
  //  @GetMapping("square/{input}")
  //  public Mono<Response> traditionalSquare(@PathVariable int input) {
  //    if(input <10 || input >20) {
  //      throw new ValidationException(input);
  //    }
  //
  //    return service.square(input);
  //  }

  @GetMapping("square/{input}")
  public Mono<Response> square(@PathVariable int input) {

    return Mono.just(input)
        .handle(
            ((integer, responseSynchronousSink) -> {
              if (integer >= 10 && integer <= 20) responseSynchronousSink.next(integer);
              else responseSynchronousSink.error(new ValidationException(integer));
            }))
        .cast(Integer.class)
        .flatMap(i -> service.square(i));
  }

  @GetMapping("square/{input}/handleWithoutHandler")
  public Mono<ResponseEntity<Response>> squareAssignment(@PathVariable int input) {

    return Mono.just(input)
            .filter(i -> i<=10 && i>=20)
            .flatMap(i -> service.square(i))
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.badRequest().build());
  }


  /*
  When you don't specify the MediaType.TEXT_EVENT_STREAM_VALUE, what it actually does it
  AbstractJackson2Encoder.encode() with collectList on Stream

  So its behavior is more like Mono<List<Response>>
   */
  @GetMapping("table/{input}")
  public Flux<Response> table(@PathVariable int input) {
    return service.table(input);
  }

  @GetMapping(value = "table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Response> stream(@PathVariable int input) {
    return service.table(input);
  }

  @PostMapping("multiply")
  public Mono<Response> multiply(
      @RequestBody Mono<Request> request, @RequestHeader Map<String, String> headers) {
    System.out.println(" headers: " + headers);
    return service.multiply(request);
  }
}

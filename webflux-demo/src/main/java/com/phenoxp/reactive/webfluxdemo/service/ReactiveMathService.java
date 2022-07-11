package com.phenoxp.reactive.webfluxdemo.service;

import com.phenoxp.reactive.webfluxdemo.model.Request;
import com.phenoxp.reactive.webfluxdemo.model.Response;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ReactiveMathService {

  public Mono<Response> square(int input) {
    return Mono.fromSupplier(() -> input * input).map(Response::new);
  }

  public Flux<Response> table(int input) {
    return Flux.range(1, 10)
        .delayElements(Duration.ofSeconds(1)) // non blocking sleep
        //                .doOnNext(i -> SleepUtil.sleepSeconds(i)) this is a blocking sleep
        .doOnNext(i -> System.out.println("Reactive Math-service processing " + i))
        .map(i -> new Response(i * input));
  }

  public Mono<Response> multiply(Mono<Request> requestMono) {
    return requestMono.map(request -> request.getFirst() * request.getSecond()).map(Response::new);
  }

  public Mono<Response> add(Mono<Request> requestMono) {
    return requestMono.map(request -> request.getFirst() + request.getSecond()).map(Response::new);
  }

  public Mono<Response> subtract(Mono<Request> requestMono) {
    return requestMono.map(request -> request.getFirst() - request.getSecond()).map(Response::new);
  }

  public Mono<Response> divide(Mono<Request> requestMono) {
    return requestMono.map(request -> request.getFirst() / request.getSecond()).map(Response::new);
  }
}

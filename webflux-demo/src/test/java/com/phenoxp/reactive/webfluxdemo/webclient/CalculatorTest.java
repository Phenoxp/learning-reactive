package com.phenoxp.reactive.webfluxdemo.webclient;

import com.phenoxp.reactive.webfluxdemo.model.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CalculatorTest extends BaseTest {

  private static final String FORMAT = "%d %s %d = %s";
  private static final int A = 10;

  @Autowired
  private WebClient webClient;

  @Test
  void calculatorTest() {
    Flux<String> flux =
        Flux.range(1, 5)
            .flatMap(b -> Flux.just("+", "-", "*", "/")
                    .flatMap(op -> send(b, op)))
            .doOnNext(System.out::println);

    StepVerifier.create(flux).expectNextCount(20).verifyComplete();
  }

  private Mono<String> send(int b, String op) {
    return webClient
        .get()
        .uri("/calculator/{first}/{second}", A, b)
        .headers(header -> header.set("x-operation", op))
        .retrieve()
        .bodyToMono(Response.class)
        .map(result -> String.format(FORMAT, A, op, b, result));
  }
}

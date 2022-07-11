package com.phenoxp.reactive.webfluxdemo.webclient;

import com.phenoxp.reactive.webfluxdemo.exception.ValidationException;
import com.phenoxp.reactive.webfluxdemo.model.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class GetWithExchangeTest extends BaseTest {

  @Autowired
  private WebClient webClient;

  @Test
  void blockBadRequestTest() {
    Mono<Object> responseMono =
        webClient
            .get()
            .uri("reactive-math/square/{number}", 3)
            .exchangeToMono(this::exchange) // exchange == retrieve + additional information http status code
            .doOnNext(System.out::println)
            .doOnError(err -> System.out.println(err.getMessage()));

    StepVerifier.create(responseMono)
            .expectNextCount(1)
            .verifyComplete();
  }

  private Mono<Object> exchange(ClientResponse clientResponse) {
    return clientResponse.rawStatusCode() == 400
        ? clientResponse.bodyToMono(ValidationException.class)
        : clientResponse.bodyToMono(Response.class);
  }
}

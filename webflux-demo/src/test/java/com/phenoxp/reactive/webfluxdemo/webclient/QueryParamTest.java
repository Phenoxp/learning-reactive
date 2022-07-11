package com.phenoxp.reactive.webfluxdemo.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.Map;

class QueryParamTest extends BaseTest {

  private static final String queryString =
      "http://localhost:8080/jobs/search?count={count}&page={page}";

  @Autowired private WebClient webClient;

  @Test
  void queryParamTest() {
    URI uri = UriComponentsBuilder.fromUriString(queryString).build(10, 20);

    Flux<Integer> integerFlux =
        webClient.get().uri(uri).retrieve().bodyToFlux(Integer.class).doOnNext(System.out::println);

    StepVerifier.create(integerFlux).expectNextCount(2).verifyComplete();
  }

  @Test
  void queryParamTest2() {
    Flux<Integer> integerFlux =
        webClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder.path("jobs/search").query("count={count}&page={page}").build(10, 20))
            .retrieve()
            .bodyToFlux(Integer.class)
            .doOnNext(System.out::println);

    StepVerifier.create(integerFlux).expectNextCount(2).verifyComplete();
  }

  @Test
  void queryParamTest3() {
    Map<String, Integer> map = Map.of("count", 10,
            "page", 20);


    Flux<Integer> integerFlux =
            webClient
                    .get()
                    .uri(
                            uriBuilder ->
                                    uriBuilder.path("jobs/search").query("count={count}&page={page}").build(map))
                    .retrieve()
                    .bodyToFlux(Integer.class)
                    .doOnNext(System.out::println);

    StepVerifier.create(integerFlux).expectNextCount(2).verifyComplete();
  }


}

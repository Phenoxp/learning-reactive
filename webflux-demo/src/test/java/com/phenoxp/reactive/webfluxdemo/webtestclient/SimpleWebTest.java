package com.phenoxp.reactive.webfluxdemo.webtestclient;

import com.phenoxp.reactive.webfluxdemo.model.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureWebTestClient
class SimpleWebTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void stepVerifierTest() {

    Flux<Response> responseMono =
        webTestClient
            .get()
            .uri("/reactive-math/square/{number}", 12)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .returnResult(Response.class)
            .getResponseBody();

    StepVerifier.create(responseMono).expectNextMatches(r -> r.getOutput() == 144).expectComplete();
  }

  @Test
  void fluentAssertionTest() {

    webTestClient
        .get()
        .uri("/reactive-math/square/{number}", 12)
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody(Response.class)
        .value(response -> assertEquals(144, response.getOutput()));
  }
}

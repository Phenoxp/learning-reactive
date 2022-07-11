package com.phenoxp.reactive.webfluxdemo.webtestclient;

import com.phenoxp.reactive.webfluxdemo.controller.ReactiveMathController;
import com.phenoxp.reactive.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(ReactiveMathController.class)
class ControllerErrorHandlingTest {

  @Autowired
  private WebTestClient client;

  @MockBean
  private ReactiveMathService reactiveMathService;

  @Test
  void errorHandlingTest() {
    client
        .get()
        .uri("/reactive-math/square/{number}", 2)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.message").isEqualTo("allowed range is 10 - 20")
        .jsonPath("$.errorCode").isEqualTo(100)
        .jsonPath("$.input").isEqualTo(2);
  }
}

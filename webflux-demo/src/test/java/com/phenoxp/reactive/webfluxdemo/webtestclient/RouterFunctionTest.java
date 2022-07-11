package com.phenoxp.reactive.webfluxdemo.webtestclient;

import com.phenoxp.reactive.webfluxdemo.config.RequestHandler;
import com.phenoxp.reactive.webfluxdemo.config.RouterConfig;
import com.phenoxp.reactive.webfluxdemo.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = RouterConfig.class)
class RouterFunctionTest {

  @Autowired private ApplicationContext applicationContext;

  @MockBean private RequestHandler requestHandler;
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build();
  }

  @Test
  void routerFunctionTest() {
    when(requestHandler.squareHandler(any()))
        .thenReturn((ServerResponse.ok().bodyValue(new Response(169))));

    webTestClient.get()
        .uri("/router/square/{input}", 13)
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(Response.class)
        .value(res -> assertEquals(169, res.getOutput()));
  }
}

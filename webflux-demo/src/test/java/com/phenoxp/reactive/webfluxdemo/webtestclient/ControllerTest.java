package com.phenoxp.reactive.webfluxdemo.webtestclient;

import com.phenoxp.reactive.webfluxdemo.controller.ParamsController;
import com.phenoxp.reactive.webfluxdemo.controller.ReactiveMathController;
import com.phenoxp.reactive.webfluxdemo.model.Response;
import com.phenoxp.reactive.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {ReactiveMathController.class, ParamsController.class})
class ControllerTest {

  @Autowired private WebTestClient webTestClient;

  @MockBean private ReactiveMathService service;

  @Test
  void square() {
    when(service.square(anyInt())).thenReturn(Mono.just(new Response(144)));

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

  @Test
  void table() {
    Flux<Response> responseFlux = Flux.range(1, 3).map(Response::new);

    when(service.table(anyInt())).thenReturn(responseFlux);

    webTestClient
        .get()
        .uri("/reactive-math/table/{number}", 12)
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Response.class)
        .hasSize(3);
  }

  @Test
  void tableStream() {
    Flux<Response> responseFlux =
        Flux.range(1, 3).map(Response::new).delayElements(Duration.ofMillis(100));

    when(service.table(anyInt())).thenReturn(responseFlux);

    webTestClient
        .get()
        .uri("/reactive-math/table/{number}/stream", 12)
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM_VALUE)
        .expectBodyList(Response.class)
        .hasSize(3);
  }

  @Test
  void queryParamTest() {
    Map<String, Integer> map = Map.of("count", 10, "page", 20);

    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/jobs/search").query("count={count}&page={page}").build(map))
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(Integer.class)
        .hasSize(2)
        .contains(10, 20);
  }
}

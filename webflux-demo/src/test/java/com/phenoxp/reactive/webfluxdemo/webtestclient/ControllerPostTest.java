package com.phenoxp.reactive.webfluxdemo.webtestclient;

import com.phenoxp.reactive.webfluxdemo.controller.ReactiveMathController;
import com.phenoxp.reactive.webfluxdemo.model.Request;
import com.phenoxp.reactive.webfluxdemo.model.Response;
import com.phenoxp.reactive.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(ReactiveMathController.class)
class ControllerPostTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ReactiveMathService reactiveMathService;


    @Test
    void postTest() {
        Request request = Request.builder().build();
        when(reactiveMathService.multiply(any())).thenReturn(Mono.just(new Response(1)));

        client.post()
                .uri("/reactive-math/multiply")
                .accept(MediaType.APPLICATION_JSON)
                .headers( httpHeaders -> httpHeaders.setBasicAuth("username", "password"))
                .headers( httpHeaders -> httpHeaders.set("someKey", "someValue"))
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful();

    }
}

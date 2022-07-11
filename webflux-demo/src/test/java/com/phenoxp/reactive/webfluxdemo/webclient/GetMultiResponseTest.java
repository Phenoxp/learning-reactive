package com.phenoxp.reactive.webfluxdemo.webclient;

import com.phenoxp.reactive.webfluxdemo.model.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class GetMultiResponseTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    void fluxTest() {
        Flux<Response> responseFlux = webClient.get()
                .uri("reactive-math/table/{number}", 13)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void fluxStreamTest() {
        Flux<Response> responseFlux = webClient.get()
                .uri("reactive-math/table/{number}/stream", 13)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();
    }
}

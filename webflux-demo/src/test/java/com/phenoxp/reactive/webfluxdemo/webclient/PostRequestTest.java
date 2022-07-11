package com.phenoxp.reactive.webfluxdemo.webclient;

import com.phenoxp.reactive.webfluxdemo.model.Request;
import com.phenoxp.reactive.webfluxdemo.model.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PostRequestTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    void postTest() {

        Mono<Response> responseMono = webClient.post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequest(7, 6))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void postHeadersTest() {
//Always do this in config, not when you have your logic

        Mono<Response> responseMono = webClient.post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequest(7, 6))
                .headers(header -> header.set("someKey", "someValue"))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    private Request buildRequest(int a, int b) {
        return Request.builder()
                .first(a)
                .second(b)
                .build();
    }
}

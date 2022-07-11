package com.phenoxp.reactive.webfluxdemo.webclient;

import com.phenoxp.reactive.webfluxdemo.model.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class GetSingleResponseTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    void blockTest() {
        Response response = webClient.get()
                .uri("reactive-math/square/{number}", 12)
                .retrieve() // here, the request is sent and this tries to get the response
                .bodyToMono(Response.class)
                .block();

        System.out.println(response);
    }

    @Test
    void blockBadRequestTest() {
        Mono<Response> response = webClient.get()
                .uri("reactive-math/square/{number}", 3)
                .retrieve() // here, the request is sent and this tries to get the response
                .bodyToMono(Response.class)
                .doOnNext(System.out::println)
                .doOnError(err -> System.out.println(err.getMessage()));

        StepVerifier.create(response)
                .verifyError(WebClientResponseException.BadRequest.class);
    }

    @Test
    void stepVerifierTest() {

        Mono<Response> responseMono = webClient.get()
                .uri("reactive-math/square/{number}", 12)
                .retrieve()
                .bodyToMono(Response.class);

        StepVerifier.create(responseMono)
                .expectNextMatches(r -> r.getOutput() == 144)
                .expectComplete();
    }
}

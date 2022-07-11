package com.phenoxp.reactive.webfluxdemo.webclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .baseUrl("http://localhost:8080")
        //                .defaultHeaders(header -> header.setBasicAuth("username", "password"))
        .filter(this::sessionToken)
        .build();
  }

  // another way;
//  // For passing token
//  private Mono<ClientResponse> sessionToken(
//      ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
//    System.out.println("Generating session token...");
//    ClientRequest request =
//        ClientRequest.from(clientRequest)
//            .headers(httpHeaders -> httpHeaders.setBearerAuth("some-jwt"))
//            .build();
//
//    return exchangeFunction.exchange(request);
//  }

  private Mono<ClientResponse> sessionToken(ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
    ClientRequest request = clientRequest.attribute("auth")
            .map(value -> value.equals("basic") ? withBasicAuth(clientRequest) : withOauth(clientRequest))
            .orElse(clientRequest);

    return exchangeFunction.exchange(request);
  }

  private ClientRequest withBasicAuth(ClientRequest clientRequest) {

    return ClientRequest.from(clientRequest)
        .headers(httpHeaders -> httpHeaders.setBasicAuth("useranem", "password"))
        .build();
  }

  private ClientRequest withOauth(ClientRequest clientRequest) {

    return ClientRequest.from(clientRequest)
            .headers(httpHeaders -> httpHeaders.setBearerAuth("some-jwt-token"))
            .build();
  }
}

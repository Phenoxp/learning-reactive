package com.phenoxp.reactive.orderservice.client;

import com.phenoxp.reactive.orderservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductClient {

  private final WebClient webClient;


  public ProductClient(@Value("${product.service.url}") String url) {
    this.webClient = WebClient.builder()
            .baseUrl(url)
            .build();
  }

  public Mono<ProductDto> getProductById(String productId) {
    return webClient.get()
            .uri("{id}", productId)
            .retrieve()
            .bodyToMono(ProductDto.class);
  }

  public Flux<ProductDto> getProducts() {
    return webClient.get()
            .retrieve()
            .bodyToFlux(ProductDto.class);
  }

}

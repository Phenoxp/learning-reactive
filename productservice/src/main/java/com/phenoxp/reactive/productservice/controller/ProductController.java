package com.phenoxp.reactive.productservice.controller;

import com.phenoxp.reactive.productservice.dto.ProductDto;
import com.phenoxp.reactive.productservice.service.ProductService;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("product")
public class ProductController {

  @Autowired
  private ProductService service;

  @GetMapping
  public Flux<ProductDto> getProducts() {

    return service.getAll();
  }

  @GetMapping("/price-range")
  public Flux<ProductDto> getProducts(
      @RequestParam("min") String min, @RequestParam("max") String max) {
    return service.getProducts(Integer.valueOf(min), Integer.valueOf(max));
  }

  @GetMapping("{id}")
  public Mono<ResponseEntity<ProductDto>> getProduct(@PathVariable String id) {
    simulateRandomException();

    return service
        .getProduct(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Mono<ProductDto> insertProduct(@RequestBody Mono<ProductDto> productDtoMono) {
    return service.addProduct(productDtoMono);
  }

  @PutMapping("{id}")
  public Mono<ResponseEntity<ProductDto>> updateProduct(
      @PathVariable String id, @RequestBody Mono<ProductDto> productDtoMono) {
    return service
        .updateProduct(id, productDtoMono)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("{id}")
  public Mono<Void> deleteProduct(@PathVariable String id) {
    return service.deleteProduct(id);
  }

  private void simulateRandomException() {
    int nextInt = ThreadLocalRandom.current().nextInt(1, 10);
    if (nextInt > 5) {
      throw new RuntimeException("Something is wrong");
    }
  }
}

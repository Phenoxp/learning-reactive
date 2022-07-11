package com.phenoxp.reactive.productservice.service;

import com.phenoxp.reactive.productservice.dto.ProductDto;
import com.phenoxp.reactive.productservice.mapper.ProductMapper;
import com.phenoxp.reactive.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

  @Autowired
  private ProductRepository repository;

  @Autowired
  private ProductMapper mapper;

  @Autowired
  private Sinks.Many<ProductDto> sinks;

  public Flux<ProductDto> getAll() {
    return repository.findAll()
            .map(mapper::map);
  }

  public Mono<ProductDto> getProduct(String id) {
    return repository
            .findById(id)
            .map(mapper::map);
  }

  public Flux<ProductDto> getProducts(Integer min, Integer max) {
    return repository.findByPriceBetween(Range.closed(min, max))
            .map(mapper::map);
  }

  public Mono<ProductDto> addProduct(Mono<ProductDto> productDtoMono) {

    return productDtoMono
        .map(mapper::map)
        .flatMap(repository::save)
        .map(mapper::map)
        .doOnNext(sinks::tryEmitNext); //Emit an item when we insert a product
  }

  public Mono<ProductDto> updateProduct(String id, Mono<ProductDto> productDtoMono) {

    return repository.findById(id)
            .flatMap(product -> productDtoMono.map(mapper::map)
                    .doOnNext(p -> p.setId(id)))
            .flatMap(repository::save)
            .map(mapper::map);
  }

  public Mono<Void> deleteProduct(String id) {
    return repository.deleteById(id);
  }
}

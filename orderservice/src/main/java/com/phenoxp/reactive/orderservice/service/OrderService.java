package com.phenoxp.reactive.orderservice.service;

import com.phenoxp.reactive.orderservice.dto.PurchaseOrderResponseDto;
import com.phenoxp.reactive.orderservice.mapper.PurchaseOrderMapper;
import com.phenoxp.reactive.orderservice.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class OrderService {

  @Autowired
  private PurchaseOrderRepository repository;

  @Autowired
  private PurchaseOrderMapper mapper;

  public Flux<PurchaseOrderResponseDto> getProductByUserId(int userId) {
    return Flux.fromStream(() -> repository.findByUserId(userId).stream())
        .map(mapper::map)
        .subscribeOn(Schedulers.boundedElastic());
  }
}

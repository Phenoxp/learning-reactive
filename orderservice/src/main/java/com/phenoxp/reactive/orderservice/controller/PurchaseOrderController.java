package com.phenoxp.reactive.orderservice.controller;

import com.phenoxp.reactive.orderservice.dto.PurchaseOrderRequestDto;
import com.phenoxp.reactive.orderservice.dto.PurchaseOrderResponseDto;
import com.phenoxp.reactive.orderservice.service.OrderFulfillmentService;
import com.phenoxp.reactive.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class PurchaseOrderController {

  @Autowired
  private OrderFulfillmentService orderFulfillmentService;

  @Autowired
  private OrderService orderService;

  @PostMapping
  public Mono<ResponseEntity<PurchaseOrderResponseDto>> order(
      @RequestBody Mono<PurchaseOrderRequestDto> requestDtoMono) {
    return orderFulfillmentService
        .processOrder(requestDtoMono)
        .map(ResponseEntity::ok)
        .onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
        .onErrorReturn(WebClientRequestException.class,
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
  }

  @GetMapping("user/{userId}")
  public Flux<PurchaseOrderResponseDto> get(@PathVariable("userId") int userId) {
    return orderService.getProductByUserId(userId);
  }
}

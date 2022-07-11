package com.phenoxp.reactive.orderservice.assignment;

import com.phenoxp.reactive.orderservice.client.ProductClient;
import com.phenoxp.reactive.orderservice.client.UserClient;
import com.phenoxp.reactive.orderservice.dto.PurchaseOrderResponseDto;
import com.phenoxp.reactive.orderservice.mapper.PurchaseOrderMapper;
import com.phenoxp.reactive.orderservice.service.OrderFulfillmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserProductTest {

  @Autowired
  private WebClient webClient;

  @Autowired
  private ProductClient productClient;

  @Autowired
  private UserClient userClient;

  @Autowired
  private OrderFulfillmentService orderFulfillmentService;

  @Autowired
  private PurchaseOrderMapper mapper;

  @Test
  void placeOrders() {
    Flux<PurchaseOrderResponseDto> dtoFlux =
        Flux.zip(userClient.getUsers(), productClient.getProducts())
            .map(tuple -> mapper.map(tuple.getT2(), tuple.getT1()))
            .flatMap(
                purchaseOrderRequestDto ->
                    orderFulfillmentService.processOrder(Mono.just(purchaseOrderRequestDto)))
            .doOnNext(System.out::println);

    StepVerifier.create(dtoFlux).expectNextCount(8).verifyComplete();
  }
}

package com.phenoxp.reactive.orderservice.service;

import com.phenoxp.reactive.orderservice.client.ProductClient;
import com.phenoxp.reactive.orderservice.client.UserClient;
import com.phenoxp.reactive.orderservice.dto.PurchaseOrderRequestDto;
import com.phenoxp.reactive.orderservice.dto.PurchaseOrderResponseDto;
import com.phenoxp.reactive.orderservice.dto.RequestContext;
import com.phenoxp.reactive.orderservice.dto.TransactionRequestDto;
import com.phenoxp.reactive.orderservice.mapper.PurchaseOrderMapper;
import com.phenoxp.reactive.orderservice.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class OrderFulfillmentService {

  @Autowired
  private ProductClient productClient;

  @Autowired
  private UserClient userClient;

  @Autowired
  private PurchaseOrderMapper purchaseOrderMapper;

  @Autowired
  private PurchaseOrderRepository purchaseOrderRepository;

  public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> requestDtoMono) {
    return requestDtoMono
        .map(RequestContext::new)
        .flatMap(this::productRequestResponse)
        .doOnNext(this::setTransactionRequestDto)
        .flatMap(this::userRequestResponse)
        .map(purchaseOrderMapper::map)
        .map(purchaseOrderRepository::save)
        .map(purchaseOrderMapper::map)
        .subscribeOn(Schedulers.boundedElastic());
  }

  private Mono<RequestContext> productRequestResponse(RequestContext context) {
    return productClient
        .getProductById(context.getPurchaseOrderRequestDto().getProductId())
        .doOnNext(context::setProductDto)
        .retryWhen(Retry.fixedDelay(5, Duration.ofMillis(100)))
        .thenReturn(context);
  }

  private Mono<RequestContext> userRequestResponse(RequestContext context) {
    return userClient
        .authorizeTransaction(context.getTransactionRequestDto())
        .doOnNext(context::setTransactionResponseDto)
        .thenReturn(context);
  }

  private void setTransactionRequestDto(RequestContext requestContext) {
    TransactionRequestDto requestDto =
        TransactionRequestDto.builder()
            .userId(requestContext.getPurchaseOrderRequestDto().getUserId())
            .amount(requestContext.getProductDto().getPrice())
            .build();

    requestContext.setTransactionRequestDto(requestDto);
  }
}

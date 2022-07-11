package com.phenoxp.reactive.userservice.controller;

import com.phenoxp.reactive.userservice.dto.TransactionRequestDto;
import com.phenoxp.reactive.userservice.dto.TransactionResponseDto;
import com.phenoxp.reactive.userservice.entity.UserTransaction;
import com.phenoxp.reactive.userservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user/transaction")
public class UserTransactionController {

  @Autowired
  private TransactionService transactionService;

  @PostMapping
  public Mono<TransactionResponseDto> createTransaction(
      @RequestBody Mono<TransactionRequestDto> requestDtoMono) {
    return requestDtoMono.flatMap(transactionService::createTransaction);
  }

  @GetMapping
  public Flux<UserTransaction> getTransactions(@RequestParam("userId") Integer userId) {
    return transactionService.getTransactions(userId);
  }
}

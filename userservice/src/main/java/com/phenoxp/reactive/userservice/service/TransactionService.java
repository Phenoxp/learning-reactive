package com.phenoxp.reactive.userservice.service;

import com.phenoxp.reactive.userservice.dto.TransactionRequestDto;
import com.phenoxp.reactive.userservice.dto.TransactionResponseDto;
import com.phenoxp.reactive.userservice.entity.UserTransaction;
import com.phenoxp.reactive.userservice.mapper.TransactionRequestMapper;
import com.phenoxp.reactive.userservice.mapper.UserMapper;
import com.phenoxp.reactive.userservice.repository.UserRepository;
import com.phenoxp.reactive.userservice.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.phenoxp.reactive.userservice.dto.TransactionStatus.APPROVED;
import static com.phenoxp.reactive.userservice.dto.TransactionStatus.DECLINED;

@Service
public class TransactionService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserTransactionRepository userTransactionRepository;

  @Autowired
  private TransactionRequestMapper transactionMapper;

  @Autowired
  private UserMapper userMapper;

  public Mono<TransactionResponseDto> createTransaction(TransactionRequestDto requestDtoMono) {
    return userRepository
        .updateUserBalance(requestDtoMono.getUserId(), requestDtoMono.getAmount())
        .filter(Boolean::booleanValue)
        .map(b -> transactionMapper.map(requestDtoMono))
        .flatMap(userTransactionRepository::save)
        .map(userTransaction -> transactionMapper.map(requestDtoMono, APPROVED))
        .defaultIfEmpty(transactionMapper.map(requestDtoMono, DECLINED));
  }

  public Flux<UserTransaction> getTransactions(Integer userId) {
    return userTransactionRepository.findByUserId(userId);

  }
}

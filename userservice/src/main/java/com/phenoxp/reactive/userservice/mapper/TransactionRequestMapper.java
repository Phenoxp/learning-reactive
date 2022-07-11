package com.phenoxp.reactive.userservice.mapper;

import com.phenoxp.reactive.userservice.dto.TransactionRequestDto;
import com.phenoxp.reactive.userservice.dto.TransactionResponseDto;
import com.phenoxp.reactive.userservice.dto.TransactionStatus;
import com.phenoxp.reactive.userservice.entity.UserTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface TransactionRequestMapper {

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(target = "transactionDate", expression = "java(LocalDateTime.now())")
    UserTransaction map(TransactionRequestDto transactionRequestDto);


    @Mapping(source ="transactionRequestDto.userId", target = "userId")
    @Mapping(source ="transactionRequestDto.amount", target = "amount")
    @Mapping(source ="transactionStatus", target = "status")
    TransactionResponseDto map(TransactionRequestDto transactionRequestDto, TransactionStatus transactionStatus);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(target ="status", constant = "APPROVED")
    TransactionResponseDto map(UserTransaction userTransaction);
}

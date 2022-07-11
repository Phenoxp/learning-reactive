package com.phenoxp.reactive.orderservice.client;

import com.phenoxp.reactive.orderservice.dto.TransactionRequestDto;
import com.phenoxp.reactive.orderservice.dto.TransactionResponseDto;
import com.phenoxp.reactive.orderservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserClient {

    private final WebClient webClient;

    public UserClient(@Value("${user.service.url}") String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Mono<TransactionResponseDto> authorizeTransaction(TransactionRequestDto transactionRequestDto) {

        return webClient.post()
                .uri("transaction")
                .bodyValue(transactionRequestDto)
                .retrieve()
                .bodyToMono(TransactionResponseDto.class);
    }

    public Flux<UserDto> getUsers() {

        return webClient.get()
                .retrieve()
                .bodyToFlux(UserDto.class);
    }
}

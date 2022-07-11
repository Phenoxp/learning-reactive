package com.phenoxp.reactive.orderservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class TransactionRequestDto {

    private Integer userId;
    private Integer amount;
}

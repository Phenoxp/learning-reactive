package com.phenoxp.reactive.webfluxdemo.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class ErrorOperator {

    private int errorCode;
    private String operator;
    private String message;
}

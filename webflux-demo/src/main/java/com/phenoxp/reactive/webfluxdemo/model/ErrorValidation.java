package com.phenoxp.reactive.webfluxdemo.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class ErrorValidation {

    private int errorCode;
    private int input;
    private String message;
}

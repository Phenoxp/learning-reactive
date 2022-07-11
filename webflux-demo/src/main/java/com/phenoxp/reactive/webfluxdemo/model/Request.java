package com.phenoxp.reactive.webfluxdemo.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Request {

    private int first;
    private int second;
}

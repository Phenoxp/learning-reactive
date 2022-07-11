package com.phenoxp.reactive.orderservice.dto;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductDto {

    private String id;
    private String description;
    private Integer price;
}

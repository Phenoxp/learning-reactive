package com.phenoxp.reactive.productservice.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@ToString
@Getter
@Setter
public class Product {

    @Id
    private String id;
    private String description;
    private Integer price;
}

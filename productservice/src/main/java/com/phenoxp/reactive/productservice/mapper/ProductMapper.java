package com.phenoxp.reactive.productservice.mapper;

import com.phenoxp.reactive.productservice.dto.ProductDto;
import com.phenoxp.reactive.productservice.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product map(ProductDto productDto);
    ProductDto map(Product product);
}

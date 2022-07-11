package com.phenoxp.reactive.orderservice.mapper;

import com.phenoxp.reactive.orderservice.dto.*;
import com.phenoxp.reactive.orderservice.entity.PurchaseOrder;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static com.phenoxp.reactive.orderservice.dto.TransactionStatus.APPROVED;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

  @Mapping(source = "purchaseOrderRequestDto.userId", target = "userId")
  @Mapping(source = "purchaseOrderRequestDto.productId", target = "productId")
  @Mapping(source = "productDto.price", target = "amount")
  PurchaseOrder map(RequestContext requestContext);

  @Mapping(source="id", target="orderId")
  PurchaseOrderResponseDto map(PurchaseOrder purchaseOrder);

  @Mapping(source="userDto.id", target = "userId")
  @Mapping(source="productDto.id", target = "productId")
  PurchaseOrderRequestDto map(ProductDto productDto, UserDto userDto);

  @AfterMapping
  default void setStatus(RequestContext requestContext, @MappingTarget PurchaseOrder purchaseOrder) {
    purchaseOrder.setStatus(
        APPROVED.equals(requestContext.getTransactionResponseDto().getStatus())
            ? OrderStatus.COMPLETED
            : OrderStatus.FAILED);
  }
}

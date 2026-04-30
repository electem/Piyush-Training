package com.order_management.dto;

import lombok.Data;

@Data
public class OrderItemDetails {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
}

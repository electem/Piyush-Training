package com.order_management.dto;


public interface TopSellingProductProjection {

    Long getProductId();

    String getProductName();

    Double getPrice();

    Integer getTotalSoldQuantity();
}
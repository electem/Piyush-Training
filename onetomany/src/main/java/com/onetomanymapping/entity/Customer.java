package com.onetomanymapping.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "cust", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Orders> orders = new ArrayList<>();

    public void addOrder(Orders order) {
        orders.add(order);
        order.setCust(this);
    }
}
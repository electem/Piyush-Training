package com.onetomanymapping.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prod;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer cust;
}
package com.order_management.repository;

import com.order_management.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.orderItems oi JOIN FETCH oi.product WHERE o.id = :id")
    Optional<Order> findOrderDetailsById(Long id);

    List<Order> findByUserId(Long userId);
}

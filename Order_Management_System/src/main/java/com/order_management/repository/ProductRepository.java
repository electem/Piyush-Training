package com.order_management.repository;

import com.order_management.dto.TopSellingProductProjection;
import com.order_management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("""
        SELECT 
            p.id AS productId,
            p.name AS productName,
            p.price AS price,
            SUM(oi.quantity) AS totalSoldQuantity
        FROM OrderItem oi
        JOIN oi.product p
        GROUP BY p.id, p.name, p.price
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<TopSellingProductProjection> findTopSellingProducts();
}

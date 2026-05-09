package com.order_management.service;

import com.order_management.dto.OrderDetailsResponse;
import com.order_management.dto.OrderItemDetails;
import com.order_management.dto.OrderItemRequest;
import com.order_management.dto.OrderRequest;
import com.order_management.entity.Order;
import com.order_management.entity.OrderItem;
import com.order_management.entity.Product;
import com.order_management.entity.User;
import com.order_management.repository.OrderRepository;
import com.order_management.repository.ProductRepository;
import com.order_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    public Order createOrder(OrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found: " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());

            double subtotal = product.getPrice() * itemRequest.getQuantity();
            totalAmount += subtotal;

            product.setStock(product.getStock() - itemRequest.getQuantity());

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    public OrderDetailsResponse getOrderDetails(Long orderId) {

        Order order = orderRepository.findOrderDetailsById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found with ID: {}", orderId);
                    return new RuntimeException("Order not found");
                });

        return mapToOrderDetailsResponse(order);
    }

    public List<OrderDetailsResponse> getAllOrdersByUser(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUserId(userId);

        List<OrderDetailsResponse> responseList = new ArrayList<>();

        for (Order order : orders) {
            responseList.add(mapToOrderDetailsResponse(order));
        }

        return responseList;
    }

    // Reusable mapper method
    private OrderDetailsResponse mapToOrderDetailsResponse(Order order) {

        OrderDetailsResponse response = new OrderDetailsResponse();

        response.setOrderId(order.getId());
        response.setOrderDate(order.getOrderDate().toString());
        response.setTotalAmount(order.getTotalAmount());

        response.setUserId(order.getUser().getId());
        response.setUserName(order.getUser().getName());
        response.setUserEmail(order.getUser().getEmail());

        List<OrderItemDetails> itemDetailsList = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            OrderItemDetails itemDetails = new OrderItemDetails();

            itemDetails.setProductId(item.getProduct().getId());
            itemDetails.setProductName(item.getProduct().getName());
            itemDetails.setQuantity(item.getQuantity());
            itemDetails.setPrice(item.getPrice());

            itemDetailsList.add(itemDetails);
        }

        response.setItems(itemDetailsList);

        return response;
    }
}
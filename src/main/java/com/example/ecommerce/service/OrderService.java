package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    
    // CRUD operations
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    Order createOrder(OrderDTO orderDTO);
    Order updateOrder(Long id, OrderDTO orderDTO);
    void deleteOrder(Long id);
    
    // Customer-specific operations
    List<Order> getOrdersByCustomerId(Long customerId);
    List<Order> getOrdersByCustomerIdAndStatus(Long customerId, Order.OrderStatus status);
    
    // Order management
    Order confirmOrder(Long orderId);
    Order shipOrder(Long orderId);
    Order deliverOrder(Long orderId);
    Order cancelOrder(Long orderId);
    
    // Status-based queries
    List<Order> getOrdersByStatus(Order.OrderStatus status);
    List<Order> getRecentOrders();
    List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // Business logic
    void calculateOrderTotal(Long orderId);
    boolean validateOrderExists(Long orderId);
    boolean validateOrderBelongsToCustomer(Long orderId, Long customerId);
}

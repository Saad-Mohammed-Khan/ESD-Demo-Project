package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.Order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Find orders by customer
    List<Order> findByCustomerId(Long customerId);
    
    // Find orders by status
    List<Order> findByStatus(OrderStatus status);
    
    // Find orders by date range
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find orders by customer and status
    List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status);
    
    // Find recent orders
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders();
    
    // Find orders by total amount range
    @Query("SELECT o FROM Order o WHERE o.totalAmount BETWEEN :minAmount AND :maxAmount")
    List<Order> findByTotalAmountRange(@Param("minAmount") java.math.BigDecimal minAmount, 
                                      @Param("maxAmount") java.math.BigDecimal maxAmount);
    
    // Count orders by status
    long countByStatus(OrderStatus status);
    
    // Find orders for a specific customer ordered by date
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.orderDate DESC")
    List<Order> findByCustomerIdOrderByOrderDateDesc(@Param("customerId") Long customerId);
}

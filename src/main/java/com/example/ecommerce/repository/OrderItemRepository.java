package com.example.ecommerce.repository;

import com.example.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    // Find order items by order
    List<OrderItem> findByOrderId(Long orderId);
    
    // Find order items by product
    List<OrderItem> findByProductId(Long productId);
    
    // Find order items by order and product
    List<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);
    
    // Find top-selling products
    @Query("SELECT oi.product, SUM(oi.quantity) as totalQuantity " +
           "FROM OrderItem oi " +
           "GROUP BY oi.product " +
           "ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProducts();
    
    // Find order items with quantity greater than specified
    List<OrderItem> findByQuantityGreaterThan(Integer quantity);
    
    // Calculate total sales for a product
    @Query("SELECT SUM(oi.quantity * oi.product.price) " +
           "FROM OrderItem oi " +
           "WHERE oi.product.id = :productId")
    java.math.BigDecimal calculateTotalSalesForProduct(@Param("productId") Long productId);
}

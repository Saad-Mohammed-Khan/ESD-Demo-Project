package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Find customer by email
    Optional<Customer> findByEmail(String email);
    
    // Find customers by name (case-insensitive)
    List<Customer> findByNameContainingIgnoreCase(String name);
    
    // Find customer by user ID
    @Query("SELECT c FROM Customer c WHERE c.user.id = :userId")
    Optional<Customer> findByUserId(@Param("userId") Long userId);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find customers with orders
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.orders o")
    List<Customer> findCustomersWithOrders();
}

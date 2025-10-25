package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ProductService productService;
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    @Override
    public Order createOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + orderDTO.getCustomerId()));
        
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        
        // Calculate total amount from order items
        if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
            for (var orderItemDTO : orderDTO.getOrderItems()) {
                Product product = productService.getProductById(orderItemDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderItemDTO.getProductId()));
                
                // Validate stock
                if (!productService.validateSufficientStock(orderItemDTO.getProductId(), orderItemDTO.getQuantity())) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getName());
                }
                
                OrderItem orderItem = new OrderItem(product, orderItemDTO.getQuantity());
                order.addOrderItem(orderItem);
            }
        }
        
        order.calculateTotalAmount();
        
        // Reduce stock for all products
        for (OrderItem orderItem : order.getOrderItems()) {
            productService.reduceStock(orderItem.getProduct().getId(), orderItem.getQuantity());
        }
        
        return orderRepository.save(order);
    }
    
    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        // Only allow updates to certain fields
        if (orderDTO.getStatus() != null) {
            order.setStatus(orderDTO.getStatus());
        }
        
        return orderRepository.save(order);
    }
    
    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomerIdAndStatus(Long customerId, Order.OrderStatus status) {
        return orderRepository.findByCustomerIdAndStatus(customerId, status);
    }
    
    @Override
    public Order confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new RuntimeException("Order cannot be confirmed. Current status: " + order.getStatus());
        }
        
        order.setStatus(Order.OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }
    
    @Override
    public Order shipOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if (order.getStatus() != Order.OrderStatus.CONFIRMED) {
            throw new RuntimeException("Order cannot be shipped. Current status: " + order.getStatus());
        }
        
        order.setStatus(Order.OrderStatus.SHIPPED);
        return orderRepository.save(order);
    }
    
    @Override
    public Order deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if (order.getStatus() != Order.OrderStatus.SHIPPED) {
            throw new RuntimeException("Order cannot be delivered. Current status: " + order.getStatus());
        }
        
        order.setStatus(Order.OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }
    
    @Override
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel a delivered order");
        }
        
        // Restore stock for cancelled orders
        if (order.getStatus() == Order.OrderStatus.CONFIRMED || order.getStatus() == Order.OrderStatus.SHIPPED) {
            for (OrderItem orderItem : order.getOrderItems()) {
                productService.addStock(orderItem.getProduct().getId(), orderItem.getQuantity());
            }
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getRecentOrders() {
        return orderRepository.findRecentOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    @Override
    public void calculateOrderTotal(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        order.calculateTotalAmount();
        orderRepository.save(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateOrderExists(Long orderId) {
        return orderRepository.existsById(orderId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateOrderBelongsToCustomer(Long orderId, Long customerId) {
        return orderRepository.findByCustomerIdAndStatus(customerId, null)
                .stream()
                .anyMatch(order -> order.getId().equals(orderId));
    }
}

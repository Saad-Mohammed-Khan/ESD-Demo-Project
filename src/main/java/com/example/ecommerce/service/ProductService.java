package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    
    // CRUD operations
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product createProduct(ProductDTO productDTO);
    Product updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    
    // Search and filter operations
    List<Product> searchProductsByName(String name);
    List<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findProductsInStock();
    List<Product> findProductsOutOfStock();
    List<Product> searchProductsByKeyword(String keyword);
    List<Product> findLowStockProducts(Integer threshold);
    
    // Stock management
    boolean isProductInStock(Long productId);
    void reduceStock(Long productId, Integer quantity);
    void addStock(Long productId, Integer quantity);
    
    // Validation
    boolean validateProductExists(Long productId);
    boolean validateSufficientStock(Long productId, Integer quantity);
}

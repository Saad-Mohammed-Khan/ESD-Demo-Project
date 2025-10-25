package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    @Override
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        return productRepository.save(product);
    }
    
    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        
        return productRepository.save(product);
    }
    
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsInStock() {
        return productRepository.findByStockQuantityGreaterThan(0);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsOutOfStock() {
        return productRepository.findByStockQuantityLessThanOrEqual(0);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProductsByKeyword(String keyword) {
        return productRepository.findByKeyword(keyword);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isProductInStock(Long productId) {
        return productRepository.findById(productId)
                .map(Product::isInStock)
                .orElse(false);
    }
    
    @Override
    public void reduceStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        
        product.reduceStock(quantity);
        productRepository.save(product);
    }
    
    @Override
    public void addStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        product.addStock(quantity);
        productRepository.save(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateProductExists(Long productId) {
        return productRepository.existsById(productId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateSufficientStock(Long productId, Integer quantity) {
        return productRepository.findById(productId)
                .map(product -> product.getStockQuantity() >= quantity)
                .orElse(false);
    }
}

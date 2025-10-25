-- Sample data for development and testing

-- Insert sample users
INSERT INTO users (username, password, email, role) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'admin@ecommerce.com', 'ADMIN'),
('customer1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'customer1@example.com', 'CUSTOMER'),
('customer2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'customer2@example.com', 'CUSTOMER');

-- Insert sample customers
INSERT INTO customers (name, email, address, user_id) VALUES 
('John Doe', 'customer1@example.com', '123 Main St, City, State 12345', 2),
('Jane Smith', 'customer2@example.com', '456 Oak Ave, City, State 67890', 3);

-- Insert sample products
INSERT INTO products (name, description, price, stock_quantity) VALUES 
('Laptop', 'High-performance laptop for work and gaming', 999.99, 50),
('Smartphone', 'Latest smartphone with advanced features', 699.99, 100),
('Headphones', 'Wireless noise-cancelling headphones', 199.99, 75),
('Tablet', '10-inch tablet perfect for reading and browsing', 299.99, 30),
('Smart Watch', 'Fitness tracking smartwatch', 149.99, 60);

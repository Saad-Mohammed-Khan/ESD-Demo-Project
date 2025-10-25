# 🛒 Spring Boot E-commerce Application

A comprehensive **Java Spring Boot E-commerce application** demonstrating **layered architecture**, **CRUD operations**, and **JWT-based security**. This project serves as a teaching example for understanding enterprise-level Spring Boot development.

## 🎯 Project Overview

This E-commerce application manages **Products**, **Customers**, **Orders**, and **OrderItems** with **user authentication** and **role-based authorization** using **JWT tokens**.

### Key Features
- ✅ **Layered Architecture** (Controller → Service → Repository → Entity)
- ✅ **JWT Authentication & Authorization**
- ✅ **Role-based Access Control** (ADMIN/CUSTOMER)
- ✅ **PostgreSQL Database Integration**
- ✅ **RESTful API Design**
- ✅ **Input Validation**
- ✅ **Transaction Management**
- ✅ **Stock Management**
- ✅ **Order Processing**

## 🏗️ Architecture

### **Entity Layer**
- **Product**: `id`, `name`, `description`, `price`, `stockQuantity`
- **User**: `id`, `username`, `password`, `email`, `role`
- **Customer**: `id`, `name`, `email`, `address`, `user` (OneToOne)
- **Order**: `id`, `orderDate`, `totalAmount`, `status`, `customer` (ManyToOne)
- **OrderItem**: `id`, `quantity`, `subtotal`, `order` (ManyToOne), `product` (ManyToOne)

### **Repository Layer**
- Extends `JpaRepository` for each entity
- Custom query methods for business logic
- Example: `ProductRepository`, `UserRepository`, `OrderRepository`

### **Service Layer**
- Interface-based design
- Business logic implementation
- Transaction management
- Example: `ProductService`, `UserService`, `OrderService`

### **Controller Layer**
- REST endpoints with proper HTTP methods
- JWT-based security
- Input validation
- Example: `ProductController`, `OrderController`, `AuthController`

## 🚀 Getting Started

### Prerequisites
- **Java 17+**
- **Maven 3.6+**
- **PostgreSQL 12+**
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code)

### Database Setup

1. **Install PostgreSQL** (if not already installed)
2. **Create Database**:
   ```sql
   CREATE DATABASE ecommerce_db;
   CREATE USER ecommerce_user WITH PASSWORD 'ecommerce_password';
   GRANT ALL PRIVILEGES ON DATABASE ecommerce_db TO ecommerce_user;
   ```

3. **Update Configuration** (if needed):
   ```yaml
   # src/main/resources/application.yml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/ecommerce_db
       username: ecommerce_user
       password: ecommerce_password
   ```

### Running the Application

1. **Clone/Download** the project to your local machine
2. **Navigate** to the project directory:
   ```bash
   cd spring-boot-ecommerce
   ```

3. **Build** the project:
   ```bash
   mvn clean install
   ```

4. **Run** the application:
   ```bash
   mvn spring-boot:run
   ```

5. **Access** the application at: `http://localhost:8080`

## 📚 API Documentation

### **Authentication Endpoints**

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "email": "user@example.com",
  "role": "CUSTOMER"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@ecommerce.com",
  "role": "ADMIN"
}
```

### **Product Endpoints**

#### Get All Products (Public)
```http
GET /api/products
```

#### Get Product by ID (Public)
```http
GET /api/products/{id}
```

#### Search Products (Public)
```http
GET /api/products/search?keyword=laptop
```

#### Create Product (Admin Only)
```http
POST /api/products
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "New Product",
  "description": "Product description",
  "price": 99.99,
  "stockQuantity": 50
}
```

#### Update Product (Admin Only)
```http
PUT /api/products/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Updated Product",
  "description": "Updated description",
  "price": 149.99,
  "stockQuantity": 75
}
```

#### Delete Product (Admin Only)
```http
DELETE /api/products/{id}
Authorization: Bearer {token}
```

### **Order Endpoints**

#### Create Order (Customer/Admin)
```http
POST /api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "customerId": 1,
  "orderItems": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

#### Get Orders by Customer
```http
GET /api/orders/customer/{customerId}
Authorization: Bearer {token}
```

#### Update Order Status
```http
POST /api/orders/{id}/confirm
POST /api/orders/{id}/ship
POST /api/orders/{id}/deliver
POST /api/orders/{id}/cancel
Authorization: Bearer {token}
```

## 🔐 Security Features

### **JWT Authentication**
- **Token-based authentication**
- **24-hour token expiration**
- **Secure password encoding** (BCrypt)

### **Role-based Authorization**
- **ADMIN**: Full access to all endpoints
- **CUSTOMER**: Limited access to customer-specific operations

### **Protected Endpoints**
- **Public**: Product browsing, authentication
- **Admin Only**: Product management, user management
- **Authenticated**: Order management

## 🧪 Testing the Application

### **Sample Data**
The application includes sample data for testing:
- **Users**: `admin`/`admin123`, `customer1`/`password123`
- **Products**: Laptop, Smartphone, Headphones, Tablet, Smart Watch
- **Customers**: John Doe, Jane Smith

### **Test Scenarios**

1. **Register a new user**
2. **Login and get JWT token**
3. **Browse products** (no authentication required)
4. **Create an order** (requires authentication)
5. **Manage orders** (admin/customer specific)

## 📁 Project Structure

```
spring-boot-ecommerce/
├── src/main/java/com/example/ecommerce/
│   ├── controller/          # REST Controllers
│   │   ├── AuthController.java
│   │   ├── ProductController.java
│   │   ├── OrderController.java
│   │   └── UserController.java
│   ├── service/             # Service Layer
│   │   ├── ProductService.java
│   │   ├── UserService.java
│   │   ├── OrderService.java
│   │   ├── JwtService.java
│   │   └── impl/            # Service Implementations
│   ├── repository/          # Data Access Layer
│   │   ├── ProductRepository.java
│   │   ├── UserRepository.java
│   │   ├── OrderRepository.java
│   │   └── OrderItemRepository.java
│   ├── entity/              # JPA Entities
│   │   ├── Product.java
│   │   ├── User.java
│   │   ├── Customer.java
│   │   ├── Order.java
│   │   └── OrderItem.java
│   ├── dto/                 # Data Transfer Objects
│   │   ├── ProductDTO.java
│   │   ├── UserDTO.java
│   │   ├── OrderDTO.java
│   │   └── LoginRequest.java
│   ├── security/            # Security Configuration
│   │   ├── SecurityConfig.java
│   │   └── JwtAuthenticationFilter.java
│   └── EcommerceApplication.java
├── src/main/resources/
│   ├── application.yml      # Main configuration
│   ├── application-dev.yml  # Development config
│   ├── application-prod.yml # Production config
│   └── data.sql            # Sample data
├── pom.xml                 # Maven configuration
└── README.md              # This file
```

## 🛠️ Development

### **Adding New Features**
1. **Create Entity** → **Repository** → **Service** → **Controller**
2. **Follow the existing patterns**
3. **Add proper validation**
4. **Update security configuration**

### **Database Migrations**
- Uses **Hibernate DDL auto-update**
- For production: Use **Flyway** or **Liquibase**

### **Environment Configuration**
- **Development**: `application-dev.yml`
- **Production**: `application-prod.yml`
- **Profiles**: `spring.profiles.active=dev`

## 🚀 Deployment

### **Docker Deployment** (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/ecommerce-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### **Environment Variables**
```bash
export DB_PASSWORD=your_secure_password
export JWT_SECRET=your_jwt_secret_key
```

## 📖 Learning Objectives

This project demonstrates:

1. **Spring Boot Fundamentals**
   - Auto-configuration
   - Starter dependencies
   - Application properties

2. **Spring Data JPA**
   - Entity relationships
   - Repository pattern
   - Custom queries

3. **Spring Security**
   - JWT authentication
   - Role-based authorization
   - Security filters

4. **RESTful API Design**
   - HTTP methods
   - Status codes
   - Request/Response patterns

5. **Enterprise Patterns**
   - Layered architecture
   - Service interfaces
   - DTO pattern
   - Transaction management

## 🤝 Contributing

This is a teaching example. Feel free to:
- **Fork** the project
- **Add features**
- **Improve documentation**
- **Submit pull requests**

## 📄 License

This project is for educational purposes. Use it to learn Spring Boot development patterns and best practices.

---

**Happy Coding! 🚀**

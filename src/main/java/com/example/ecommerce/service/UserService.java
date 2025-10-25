package com.example.ecommerce.service;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.LoginResponse;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    // Authentication
    LoginResponse login(LoginRequest loginRequest);
    UserDTO register(UserDTO userDTO);
    
    // CRUD operations
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    User updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    
    // Validation
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // Password management
    String encodePassword(String password);
    boolean matchesPassword(String rawPassword, String encodedPassword);
}

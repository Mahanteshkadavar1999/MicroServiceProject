package com.microservice.service;

import com.microservice.entity.User;
import com.microservice.exception.InvalidCredentialsException;
import com.microservice.exception.UserAlreadyExistsException;
import com.microservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    public String register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Generate token with customerId (user ID)
        return jwtService.generateToken(savedUser.getUsername(), savedUser.getId());
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // Generate token with customerId (user ID)
        return jwtService.generateToken(username, user.getId());
    }

    public Long getCustomerId(String userName) {
        User user = userRepository.findByUsername(userName);
        if(null == user){
            throw new RuntimeException("Customer not found for the name :"+userName);
        }
        return user.getId();
    }
}
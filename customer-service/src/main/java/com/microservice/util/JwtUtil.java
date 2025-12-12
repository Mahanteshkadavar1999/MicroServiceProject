package com.microservice.util;

import com.microservice.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Autowired
    private JwtService jwtService;

    public boolean validateToken(String token) {
        try {
            jwtService.validateToken(token);
            return true;
        } catch (Exception e) {
            logger.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public Long getCustomerIdFromToken(String token) {
        try {
            return jwtService.extractCustomerId(token);
        } catch (Exception e) {
            logger.error("Error extracting customer ID from token", e);
            throw new RuntimeException("Invalid token");
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            return jwtService.extractUsername(token);
        } catch (Exception e) {
            logger.error("Error extracting username from token", e);
            throw new RuntimeException("Invalid token");
        }
    }

    public String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header. Expected format: 'Bearer <token>'");
        }
        return authHeader.substring(7);
    }

    public Long getCustomerIdFromHeader(String authHeader) {
        try {
            String token = extractTokenFromHeader(authHeader);
            if (!validateToken(token)) {
                throw new RuntimeException("Invalid or expired token");
            }
            return getCustomerIdFromToken(token);
        } catch (Exception e) {
            logger.error("Failed to extract customer ID from header", e);
            throw new RuntimeException("Authentication failed", e);
        }
    }
}
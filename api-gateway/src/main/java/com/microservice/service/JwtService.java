package com.microservice.service;

import com.microservice.exception.JwtProcessingException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username, Long customerId) {
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .claim("customerId", customerId)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new JwtProcessingException("Failed to generate JWT");
        }
    }

    public String validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new JwtProcessingException("JWT token expired");
        } catch (MalformedJwtException e) {
            throw new JwtProcessingException("Malformed JWT token");
        } catch (SignatureException e) {
            throw new JwtProcessingException("Invalid JWT signature");
        } catch (Exception e) {
            throw new JwtProcessingException("Invalid JWT token");
        }
    }

    public Long extractCustomerId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("customerId", Long.class);
        } catch (Exception e) {
            throw new JwtProcessingException("Failed to extract customer ID from token");
        }
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new JwtProcessingException("Failed to extract username from token");
        }
    }
}
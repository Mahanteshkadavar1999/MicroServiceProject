package com.microservice.config;

import com.microservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        // Skip JWT validation for public endpoints
        if (isPublicEndpoint(requestURI)) {
            logger.debug("Skipping JWT validation for public endpoint: {} {}", method, requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if Authorization header is present and valid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header for protected endpoint: {} {}", method, requestURI);
            sendUnauthorizedError(response, "Missing or invalid Authorization header. Required format: 'Bearer <token>'");
            return;
        }

        token = authHeader.substring(7);

        try {
            username = jwtService.validateToken(token);
            logger.info("Token validated for username: {}", username);
        } catch (Exception e) {
            logger.warn("Invalid JWT token format for endpoint: {} {}", method, requestURI);
            sendUnauthorizedError(response, "Invalid JWT token format");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Validate token expiration and signature
                if (jwtService.extractUsername(token).equals(username)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, null);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("JWT authentication successful for user: {} on endpoint: {}", username, requestURI);
                } else {
                    logger.warn("Invalid JWT token for user: {}", username);
                    sendUnauthorizedError(response, "Invalid or expired token");
                    return;
                }
            } catch (Exception e) {
                logger.error("JWT authentication failed for user: {}", username, e);
                sendUnauthorizedError(response, "Authentication failed: " + e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String requestURI) {
        return requestURI.startsWith("/auth/") ||
                requestURI.startsWith("/swagger-ui/") ||
                requestURI.startsWith("/api-docs") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.equals("/swagger-ui.html") ||
                requestURI.startsWith("/actuator/");
    }

    private void sendUnauthorizedError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format(
                "{\"error\": \"Unauthorized\", \"message\": \"%s\", \"status\": 401}",
                message
        );

        response.getWriter().write(jsonResponse);
    }
}
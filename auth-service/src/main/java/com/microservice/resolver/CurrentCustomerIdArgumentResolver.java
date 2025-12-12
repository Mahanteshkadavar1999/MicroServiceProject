package com.microservice.resolver;

import com.microservice.annotation.CurrentCustomerId;
import com.microservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class CurrentCustomerIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private JwtService jwtService;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentCustomerId.class) != null &&
                parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        if (request.getRequestURI().contains("/actuator/")) {
            return 0L; // Return default for health checks
        }

        // Try to get customer ID from X-USERNAME header (set by gateway)
        String usernameHeader = request.getHeader("X-USERNAME");

        if (usernameHeader != null && !usernameHeader.isEmpty()) {
            // Convert username to customer ID (you might need a different logic based on your requirements)
            // For now, assuming username is the customer ID or can be converted to it
            try {
                return Long.parseLong(usernameHeader);
            } catch (NumberFormatException e) {
                // If username is not a number, you might need to look up the customer ID from database
                // For simplicity, we'll return 1L as default
                return 1L;
            }
        }

        // Fallback: Extract from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            System.out.println(username);

            Long customerIdByName = getCustomerIdByName(username);
            System.out.println("--------------------"+customerIdByName);
            // Convert username to customer ID
            // In a real application, you would query the database to get customer ID from username
            // For now, return a default or implement your own logic
            try {
                return customerIdByName;
            } catch (NumberFormatException e) {
                return 1L; // Default customer ID
            }
        }

        throw new IllegalArgumentException("Unable to extract customer ID from request");
    }

    private Long getCustomerIdByName(String username) {
        return webClientBuilder.build().get()
                .uri("http://auth-service/auth/get-customer-id?username=" + username)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

}
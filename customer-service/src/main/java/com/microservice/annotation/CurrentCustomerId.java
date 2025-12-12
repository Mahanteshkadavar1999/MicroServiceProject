package com.microservice.annotation;

import java.lang.annotation.*;

/**
 * Annotation to extract customer ID from JWT token in Authorization header
 * Usage:
 * public ResponseEntity<?> someMethod(@CurrentCustomerId Long customerId) { ... }
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentCustomerId {
}
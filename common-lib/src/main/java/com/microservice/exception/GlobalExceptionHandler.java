package com.microservice.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Common Response Format
    private Map<String, Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error.put("timestamp", LocalDateTime.now());
        return error;
    }

    // Handle Username Already Exists, Invalid Credentials, Custom Errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // Handle Validation Errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(
                buildResponse(HttpStatus.BAD_REQUEST, msg),
                HttpStatus.BAD_REQUEST
        );
    }

    // JWT Expired
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwt(ExpiredJwtException ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.UNAUTHORIZED, "JWT Token expired"),
                HttpStatus.UNAUTHORIZED
        );
    }

    // Invalid JWT Signature
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleSignature(SignatureException ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.UNAUTHORIZED, "Invalid JWT signature"),
                HttpStatus.UNAUTHORIZED
        );
    }

    // Unsupported JWT Format
    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<?> handleUnsupportedJwt(UnsupportedJwtException ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.UNAUTHORIZED, "Unsupported JWT token"),
                HttpStatus.UNAUTHORIZED
        );
    }

    // Malformed JWT Token
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> handleMalformedJwt(MalformedJwtException ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.UNAUTHORIZED, "Malformed JWT token"),
                HttpStatus.UNAUTHORIZED
        );
    }

    // Catch-all for any other exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserExists(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCreds(InvalidCredentialsException ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(JwtProcessingException.class)
    public ResponseEntity<?> handleJwtErrors(JwtProcessingException ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }
}


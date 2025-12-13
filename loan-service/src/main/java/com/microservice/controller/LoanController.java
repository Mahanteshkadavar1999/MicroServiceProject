package com.microservice.controller;

import com.microservice.dto.LoanRequest;
import com.microservice.entity.Loan;
import com.microservice.service.JwtService;
import com.microservice.service.LoanService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
public class LoanController {

    private static final Logger log = LoggerFactory.getLogger(LoanController.class);

    @Autowired
    LoanService loanService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/apply")
    @CircuitBreaker(name="loanCb",fallbackMethod = "fallbackMethodForLoan")
    public ResponseEntity<?> applyLoan(@RequestHeader("Authorization") String authHeader,
                                    @Valid @RequestBody LoanRequest req) throws Exception {
        String token = authHeader.substring(7);
        Long customerId = jwtService.extractCustomerId(token);
        log.info("API: Apply Loan for customer {}", customerId);
        ResponseEntity<?> loan = loanService.applyLoan(customerId, req, token);
        log.info("loan details are{}", loan);
        return loanService.applyLoan(customerId, req,token);
    }

    public ResponseEntity<?> fallbackMethodForLoan(String authHeader,
                                                   LoanRequest req,
                                                   Throwable ex) {

        log.error("Fallback executed due to: {}", ex.getMessage());
        throw new RuntimeException("Customer-service is DOWN. Please try again later."+req);
    }

}


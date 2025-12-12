package com.microservice.controller;

import com.microservice.dto.LoanRequest;
import com.microservice.entity.Loan;
import com.microservice.service.JwtService;
import com.microservice.service.LoanService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Loan applyLoan(@RequestHeader("Authorization") String authHeader,
                          @Valid @RequestBody LoanRequest req) {
        String token = authHeader.substring(7);
        Long customerId = jwtService.extractCustomerId(token);
        log.info("API: Apply Loan for customer {}", customerId);
        return loanService.applyLoan(customerId, req,token);
    }
}


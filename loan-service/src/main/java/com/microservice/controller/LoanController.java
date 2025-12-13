package com.microservice.controller;

import com.microservice.dto.LoanRequest;
import com.microservice.entity.Loan;
import com.microservice.repository.LoanRepository;
import com.microservice.service.JwtService;
import com.microservice.service.LoanService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/loan")
public class LoanController {

    private static final Logger log = LoggerFactory.getLogger(LoanController.class);

    private final LoanService loanService;
    private final JwtService jwtService;
    private final LoanRepository loanRepository;
    private final WebClient webClient;

    public LoanController(LoanService loanService, JwtService jwtService,
                          LoanRepository loanRepository, @LoadBalanced WebClient.Builder webClientBuilder) {
        this.loanService = loanService;
        this.jwtService = jwtService;
        this.loanRepository = loanRepository;
        this.webClient = webClientBuilder.build();
    }

    @PostMapping("/apply")
    public CompletableFuture<ResponseEntity<String>> applyLoan(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody LoanRequest req) {

        log.info("=== LOAN APPLICATION STARTED ===");

        try {
            String token = authHeader.substring(7);
            Long customerId = jwtService.extractCustomerId(token);
            log.info("Customer ID  extracted: {}", customerId);

            return loanService.applyLoan(customerId, req, token);

        } catch (Exception e) {
            log.error("Error in loan application: {}", e.getMessage());
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest()
                            .body("Invalid request: " + e.getMessage())
            );
        }
    }

    @PostMapping("/apply-test")
    public ResponseEntity<String> applyLoanTest(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody LoanRequest req) {

        log.info("=== TEST LOAN APPLICATION STARTED (NO TIMELIMITER) ===");

        try {
            String token = authHeader.substring(7);
            Long customerId = jwtService.extractCustomerId(token);
            log.info("Customer ID extracted: {}", customerId);

            // Simulate synchronous call without TimeLimiter
            Boolean isPanPresent = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("customer-service")
                            .path("/customer/pan")
                            .queryParam("currentCustomerId", customerId)
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(); // Synchronous call

            if (Boolean.TRUE.equals(isPanPresent)) {
                Loan loan = new Loan();
                loan.setCustomerId(customerId);
                loan.setLoanAmount(req.getLoanAmount());
                loan.setTenureMonths(req.getTenureMonths());
                loan.setLoanType(req.getLoanType());
                loan.setRequestedDate(LocalDate.now());
                loan.setStatus("PENDING");

                loanRepository.save(loan);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("TEST: Loan application submitted successfully");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("TEST: PAN details not found");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in test loan application: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("TEST Error: " + e.getMessage());
        }
    }
}
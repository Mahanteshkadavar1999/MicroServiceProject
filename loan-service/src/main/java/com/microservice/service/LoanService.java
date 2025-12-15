package com.microservice.service;

import com.microservice.dto.LoanRequest;
import com.microservice.entity.Loan;
import com.microservice.repository.LoanRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class LoanService {

    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

    private final LoanRepository loanRepository;
    private final WebClient webClient;

    public LoanService(LoanRepository loanRepository, @LoadBalanced WebClient.Builder webClientBuilder) {
        this.loanRepository = loanRepository;
        this.webClient = webClientBuilder.build();
    }

    @CircuitBreaker(name = "customerService", fallbackMethod = "applyLoanFallback")
    @TimeLimiter(name = "customerService", fallbackMethod = "applyLoanFallback")
    @Retry(name = "customerService", fallbackMethod = "applyLoanFallback")
    public CompletableFuture<ResponseEntity<String>> applyLoan(
            Long customerId, LoanRequest req, String token) {

        log.info("Applying loan for customer {} at {}", customerId, LocalDateTime.now());

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("customer-service")
                        .path("/customer/pan")
                        .queryParam("currentCustomerId", customerId)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .map(isPanPresent -> {
                    log.info("PAN check completed for customer {}: {}", customerId, isPanPresent);
                    return processLoanStringResponse(customerId, req, isPanPresent);
                })
                .toFuture()
                // Add this to handle the CompletableFuture properly
                .thenApply(response -> {
                    log.info("Sending response to client for customer {}", customerId);
                    return response;
                })
                .exceptionally(ex -> {
                    log.error("Exception in applyLoan for customer {}: {}", customerId, ex.getMessage());
                    // If fallback didn't catch it, return a proper error
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Internal server error: " + ex.getMessage());
                });
    }

    private ResponseEntity<String> processLoanStringResponse(
            Long customerId, LoanRequest req, Boolean isPanPresent) {

        log.info("Processing loan for customer {}: PAN present = {}", customerId, isPanPresent);

        if (Boolean.TRUE.equals(isPanPresent)) {
            Loan loan = new Loan();
            loan.setCustomerId(customerId);
            loan.setLoanAmount(req.getLoanAmount());
            loan.setTenureMonths(req.getTenureMonths());
            loan.setLoanType(req.getLoanType());
            loan.setRequestedDate(
                    req.getRequestedDate() != null ? req.getRequestedDate() : LocalDate.now());
            loan.setStatus("PENDING");

            Loan savedLoan = loanRepository.save(loan);
            log.info("Loan saved with ID: {} for customer: {}", savedLoan.getId(), customerId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Loan application submitted successfully for customer " + customerId);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Customer details incomplete for user " + customerId);
    }

    public CompletableFuture<ResponseEntity<String>> applyLoanFallback(
            Long customerId, LoanRequest req, String token, Throwable ex) {

        log.error("FALLBACK TRIGGERED at {} for customer {} due to: {}",
                LocalDateTime.now(), customerId, ex.getClass().getSimpleName());
        log.error("Exception details: {}", ex.getMessage());

        // Always return 408 for timeout to see if Postman shows it
        return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT) // 408
                        .body("Request timeout (408). The PAN verification is taking longer than expected.")
        );
    }
}
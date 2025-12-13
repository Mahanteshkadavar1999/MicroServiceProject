package com.microservice.service;

import com.microservice.dto.LoanRequest;
import com.microservice.entity.Loan;
import com.microservice.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class LoanService {

    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private WebClient.Builder webClient;

    public ResponseEntity<?> applyLoan(Long customerId, LoanRequest req, String token) {

        log.info("Applying loan for customer {}", customerId);

        // ========= Call customer-service to validate PAN details =========
        Boolean isPanDetailsPresent;

        try {
            isPanDetailsPresent = webClient.build()
                    .get()
                    .uri("http://customer-service/customer/pan?currentCustomerId=" + customerId)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

        } catch (Exception ex) {
            log.error("Failed to contact customer-service for PAN check: {}", ex.getMessage());
            log.error("Failed to connect to customer-service: {}", ex.getMessage());
            throw new RuntimeException("Customer service is down");
        }

        log.info("PAN details present for customer {} → {}", customerId, isPanDetailsPresent);

        // ========= Validate customer details =========
        if (Boolean.TRUE.equals(isPanDetailsPresent)) {

            Loan loan = new Loan();
            loan.setCustomerId(customerId);
            loan.setLoanAmount(req.getLoanAmount());
            loan.setTenureMonths(req.getTenureMonths());
            loan.setLoanType(req.getLoanType());
            loan.setRequestedDate(req.getRequestedDate() != null ? req.getRequestedDate() : LocalDate.now());
            loan.setStatus("PENDING");

            loanRepository.save(loan);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Loan application submitted successfully for customer " + customerId);
        }

        // ========= If PAN is not present =========
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Customer details incomplete for user " + customerId);
    }
}

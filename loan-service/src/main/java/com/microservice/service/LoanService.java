package com.microservice.service;

import com.microservice.dto.LoanRequest;
import com.microservice.entity.Loan;
import com.microservice.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class LoanService {

    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    WebClient.Builder webClient;

    public Loan applyLoan(Long customerId, LoanRequest req,String token) {
        log.info("Applying loan for customer {}", customerId);

        // Call customer-service APIs to check if all details are present
        Boolean isPanDetailsPresent = Objects.requireNonNull(webClient.build().get()
                .uri("http://customer-service/customer/pan?currentCustomerId=" + customerId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());
/*
        Boolean isIncomeDetailsPresent = Objects.requireNonNull(webClient.build()..get()
                .uri("http://localhost:8883/customer/income")
                .header("X-CUSTOMER-ID", customerId.toString())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());

        Boolean isAddressDetailsPresent = Objects.requireNonNull(webClient.build()..get()
                .uri("http://localhost:8883/customer/address")
                .header("X-CUSTOMER-ID", customerId.toString())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());*/

        log.info("PAN DETAILS ARE PRESENT FOR THE USER {} IS {}",customerId,isPanDetailsPresent);
        if (isPanDetailsPresent) {
            Loan loan = new Loan();
            loan.setCustomerId(customerId);
            loan.setLoanAmount(req.getLoanAmount());
            loan.setTenureMonths(req.getTenureMonths());
            loan.setLoanType(req.getLoanType());
            loan.setRequestedDate(req.getRequestedDate() != null ? req.getRequestedDate() : LocalDate.now());
            loan.setStatus("PENDING");
            return loanRepository.save(loan);
        } else {
            throw new RuntimeException("Customer details incomplete for user " + customerId);
        }
    }
}
package com.microservice.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanRequest {
    @NotNull
    private BigDecimal loanAmount;

    @NotNull
    private Integer tenureMonths;

    private String loanType;
    private LocalDate requestedDate;

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    @Override
    public String toString() {
        return "LoanRequest{" +
                "loanAmount=" + loanAmount +
                ", tenureMonths=" + tenureMonths +
                ", loanType='" + loanType + '\'' +
                ", requestedDate=" + requestedDate +
                '}';
    }
}

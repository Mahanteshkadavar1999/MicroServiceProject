package com.microservice.dto;

import jakarta.validation.constraints.*;

public class PanDetailsRequest {

    private Long customerId;

    @NotBlank(message = "PAN number is required")
    private String panNumber;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;

    @NotNull(message = "Loan amount is required")
    private Double loanAmount;

    @NotBlank(message = "Gender is required")
    private String gender;

    // Getters and Setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(Double loanAmount) { this.loanAmount = loanAmount; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}

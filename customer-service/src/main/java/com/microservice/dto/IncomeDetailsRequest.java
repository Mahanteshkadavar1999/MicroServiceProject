package com.microservice.dto;

import jakarta.validation.constraints.*;

public class IncomeDetailsRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Employment type is required")
    private String employmentType;

    @NotBlank(message = "Profession type is required")
    private String professionType;

    private String education;

    private String companyName;

    private String uanNumber;

    private String dateOfJoining;

    @NotNull(message = "Annual income is required")
    private Double annualIncome;

    @NotNull(message = "Annual household income is required")
    private Double annualHouseHoldIncome;

    // Getters and Setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public String getProfessionType() { return professionType; }
    public void setProfessionType(String professionType) { this.professionType = professionType; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getUanNumber() { return uanNumber; }
    public void setUanNumber(String uanNumber) { this.uanNumber = uanNumber; }

    public String getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(String dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public Double getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(Double annualIncome) { this.annualIncome = annualIncome; }

    public Double getAnnualHouseHoldIncome() { return annualHouseHoldIncome; }
    public void setAnnualHouseHoldIncome(Double annualHouseHoldIncome) { this.annualHouseHoldIncome = annualHouseHoldIncome; }
}


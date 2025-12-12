package com.microservice.entity;

import jakarta.persistence.*;

@Entity
public class IncomeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String employmentType;
    private String professionType;
    private String education;
    private String companyName;
    private String uanNumber;
    private String dateOfJoining;
    private Double AnnualIncome;
    private Double AnnualHouseHoldIncome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getProfessionType() {
        return professionType;
    }

    public void setProfessionType(String professionType) {
        this.professionType = professionType;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUanNumber() {
        return uanNumber;
    }

    public void setUanNumber(String uanNumber) {
        this.uanNumber = uanNumber;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Double getAnnualIncome() {
        return AnnualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        AnnualIncome = annualIncome;
    }

    public Double getAnnualHouseHoldIncome() {
        return AnnualHouseHoldIncome;
    }

    public void setAnnualHouseHoldIncome(Double annualHouseHoldIncome) {
        AnnualHouseHoldIncome = annualHouseHoldIncome;
    }

    @Override
    public String toString() {
        return "IncomeDetails{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", employmentType='" + employmentType + '\'' +
                ", professionType='" + professionType + '\'' +
                ", education='" + education + '\'' +
                ", companyName='" + companyName + '\'' +
                ", uanNumber='" + uanNumber + '\'' +
                ", dateOfJoining='" + dateOfJoining + '\'' +
                ", AnnualIncome=" + AnnualIncome +
                ", AnnualHouseHoldIncome=" + AnnualHouseHoldIncome +
                '}';
    }
}


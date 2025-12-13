package com.microservice.service;

import com.microservice.dto.AddressDetailsRequest;
import com.microservice.dto.IncomeDetailsRequest;
import com.microservice.dto.PanDetailsRequest;
import com.microservice.entity.AddressDetails;
import com.microservice.entity.IncomeDetails;
import com.microservice.entity.PanDetails;
import com.microservice.repository.AddressDetailsRepository;
import com.microservice.repository.IncomeDetailsRepository;
import com.microservice.repository.PanDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    PanDetailsRepository panRepo;

    @Autowired
    IncomeDetailsRepository incomeRepo;

    @Autowired
    AddressDetailsRepository addressRepo;

    // ---------------- PAN DETAILS ----------------
    public PanDetails savePan(PanDetailsRequest req,Long customerId) {
        log.info("Saving PAN details for customer {}", customerId);

        PanDetails p = new PanDetails();
        p.setCustomerId(customerId);
        p.setPanNumber(req.getPanNumber());
        p.setFullName(req.getFullName());
        p.setDateOfBirth(req.getDateOfBirth());
        p.setLoanAmount(req.getLoanAmount());
        p.setGender(req.getGender());

        return panRepo.save(p);
    }

    // ---------------- INCOME DETAILS ----------------
    public IncomeDetails saveIncome(IncomeDetailsRequest req,Long customerId) {
        log.info("Saving Income details for customer {}", customerId);

        IncomeDetails i = new IncomeDetails();
        i.setCustomerId(customerId);
        i.setEmploymentType(req.getEmploymentType());
        i.setProfessionType(req.getProfessionType());
        i.setEducation(req.getEducation());
        i.setCompanyName(req.getCompanyName());
        i.setUanNumber(req.getUanNumber());
        i.setDateOfJoining(req.getDateOfJoining());
        i.setAnnualIncome(req.getAnnualIncome());
        i.setAnnualHouseHoldIncome(req.getAnnualHouseHoldIncome());

        return incomeRepo.save(i);
    }

    // ---------------- ADDRESS DETAILS ----------------
    public AddressDetails saveAddress(AddressDetailsRequest req,Long customerId) {
        log.info("Saving Address details for customer {}", customerId);

        AddressDetails a = new AddressDetails();
        a.setCustomerId(customerId);
        a.setAddressLine1(req.getAddressLine1());
        a.setAddressLine2(req.getAddressLine2());
        a.setCity(req.getCity());
        a.setState(req.getState());
        a.setPincode(req.getPincode());

        return addressRepo.save(a);
    }

    public Boolean getPanDetails(Long customerId) {
        log.info("customer id is {}", customerId);
        Optional<PanDetails> panDetails = panRepo.findTopByCustomerIdOrderByIdDesc(customerId);
        log.info("customer pan details are  {}", panDetails);
        return panDetails.isPresent();
    }

    public Boolean getIncomeDetails(Long customerId) {
        IncomeDetails incomeDetails = incomeRepo.findByCustomerId(customerId);
        return null == incomeDetails;
    }

    public boolean getAddressDetails(Long customerId) {
        AddressDetails addressDetails = addressRepo.findByCustomerId(customerId);
        return null == addressDetails;
    }
}




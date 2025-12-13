package com.microservice.controller;

import com.microservice.annotation.CurrentCustomerId;
import com.microservice.dto.AddressDetailsRequest;
import com.microservice.dto.IncomeDetailsRequest;
import com.microservice.dto.PanDetailsRequest;
import com.microservice.entity.AddressDetails;
import com.microservice.entity.IncomeDetails;
import com.microservice.entity.PanDetails;
import com.microservice.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);


    @Autowired
    CustomerService customerService;

    @PostMapping("/pan")
    public PanDetails savePan(@CurrentCustomerId Long currentCustomerId,
                              @Valid @RequestBody PanDetailsRequest req) {
        log.info(" customer id is : {}", currentCustomerId);
        log.info("API: Save PAN details");
        return customerService.savePan(req,currentCustomerId);
    }

    @GetMapping("/pan")
    public Boolean getPanDetails(@RequestParam Long currentCustomerId) throws Exception {
        log.info("API: Get PAN details");
        return customerService.getPanDetails(currentCustomerId);
    }

    @PostMapping("/income")
    public IncomeDetails saveIncome(@CurrentCustomerId Long currentCustomerId,
                                    @Valid @RequestBody IncomeDetailsRequest req) {
        log.info("API: Save Income details");
        return customerService.saveIncome(req,currentCustomerId);
    }

    @GetMapping("/income")
    public Boolean getIncomeDetails(@CurrentCustomerId Long currentCustomerId) {
        log.info("API: Get Income details");
        return customerService.getIncomeDetails(currentCustomerId);
    }

    @PostMapping("/address")
    public AddressDetails saveAddress(@CurrentCustomerId Long currentCustomerId,
                                      @Valid @RequestBody AddressDetailsRequest req) {
        log.info("API: Save Address details");
        return customerService.saveAddress(req,currentCustomerId);
    }

    @GetMapping("/address")
    public Boolean getAddressDetails(@CurrentCustomerId Long currentCustomerId) {
        log.info("API: Get Address details");
        return customerService.getAddressDetails(currentCustomerId);
    }
}


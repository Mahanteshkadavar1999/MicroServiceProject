package com.microservice.repository;

import com.microservice.entity.AddressDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressDetailsRepository extends JpaRepository<AddressDetails, Long> {
    AddressDetails findByCustomerId(Long customerId);
}


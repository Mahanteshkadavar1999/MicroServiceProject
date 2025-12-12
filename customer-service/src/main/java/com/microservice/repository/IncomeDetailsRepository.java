package com.microservice.repository;

import com.microservice.entity.IncomeDetails;
import com.microservice.entity.PanDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeDetailsRepository extends JpaRepository<IncomeDetails, Long> {
    IncomeDetails findByCustomerId(Long customerId);
}


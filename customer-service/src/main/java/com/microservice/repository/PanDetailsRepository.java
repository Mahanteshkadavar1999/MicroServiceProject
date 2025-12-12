package com.microservice.repository;

import com.microservice.entity.PanDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PanDetailsRepository extends JpaRepository<PanDetails, Long> {
    Optional<PanDetails> findTopByCustomerIdOrderByIdDesc(Long customerId);
}


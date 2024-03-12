package com.meghana.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meghana.ecommerce.dto.PaymentDetails;

public interface DetailsRepository extends JpaRepository<PaymentDetails, Integer>{

}

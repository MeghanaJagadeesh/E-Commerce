package com.meghana.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meghana.ecommerce.dto.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	// who is giving implementation for this 

	List<Customer> findByEmailOrPhoneNum(String email, long phoneNum);
}

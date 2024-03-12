package com.meghana.ecommerce.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meghana.ecommerce.dto.PaymentDetails;
import com.meghana.ecommerce.repository.DetailsRepository;

@Component
public class PaymentDao {

	@Autowired
	DetailsRepository repository;

	public void save(PaymentDetails details) {
		repository.save(details);

	}

	public PaymentDetails findById(int id) {
		return repository.findById(id).orElse(null);
	}
}

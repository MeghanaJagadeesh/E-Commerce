package com.meghana.ecommerce.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meghana.ecommerce.dto.Customer;
import com.meghana.ecommerce.dto.OrderDetails;
import com.meghana.ecommerce.repository.OrderDetailsRepository;

@Component
public class OrderDetailsDao {

	@Autowired
	OrderDetailsRepository repository;

	public void save(OrderDetails orderDetails) {
		repository.save(orderDetails);

	}

	public List<OrderDetails> fetchByCustomer(Customer customer) {
		return repository.findByCustomer(customer);
	}

	public OrderDetails findById(int id) {
		return repository.findById(id).get();
	}

}

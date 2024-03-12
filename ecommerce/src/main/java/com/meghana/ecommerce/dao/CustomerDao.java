package com.meghana.ecommerce.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meghana.ecommerce.dto.Customer;
import com.meghana.ecommerce.dto.Product;
import com.meghana.ecommerce.repository.CustomerRepository;

@Component
public class CustomerDao {

	@Autowired
	ProductDao productDao;

	@Autowired
	CustomerRepository customerRepository;

	public List<Customer> findByEmailOrPhoneNum(String email, long phoneNum) {
		return customerRepository.findByEmailOrPhoneNum(email, phoneNum);
	}

	public void save(Customer customer) {
		customerRepository.save(customer);

	}

	public void update(Customer customer) {
		customerRepository.save(customer);

	}

	public Customer findById(int id) {
		return customerRepository.findById(id).orElse(null); // if id is present it will send the object or else it send
																// null
	}

	public List<Product> findByIfDisplayTrue() {
		return productDao.findByIfDisplayTrue();
	}

	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	public void delete(int id) {
		customerRepository.deleteById(id);
		
	}

}

package com.meghana.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meghana.ecommerce.dto.Customer;
import com.meghana.ecommerce.dto.OrderDetails;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {

	List<OrderDetails> findByCustomer(Customer customer);

}

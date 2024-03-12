package com.meghana.ecommerce.dto;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Component
public class PaymentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String keyCode;
	double amount;
	String currency;
	String name;
	String description;
	String image;
	String order_id;
	String payment_id;
	String status;

}

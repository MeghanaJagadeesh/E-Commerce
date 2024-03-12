package com.meghana.ecommerce.dto;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class CustomerCart {
	@Id
	private int id;

	//one cart can have multilple products 
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<Items> item;		
	
	double totalAmount;	
}

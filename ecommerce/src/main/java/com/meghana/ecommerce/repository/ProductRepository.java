package com.meghana.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meghana.ecommerce.dto.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

	public List<Product> findByProductCategory(String productCategory);
	public List<Product> findByDisplayTrue();
	public Product findByProductName(String productName);
}

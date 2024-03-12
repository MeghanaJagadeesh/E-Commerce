package com.meghana.ecommerce.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.meghana.ecommerce.dto.Product;
import com.meghana.ecommerce.repository.ProductRepository;

@Component
public class ProductDao {

	@Autowired
	ProductRepository repository;

	public void save(Product product) {
		repository.save(product);
	}

	public List<Product> fetchAllProducts() {
		return repository.findAll();
	}

	public Product findById(int id) {
		return repository.findById(id).orElse(null);
	}

	public void delete(int id) {
		repository.deleteById(id);
	}

	public List<Product> fetchProduct(String productCategory) {
		return repository.findByProductCategory(productCategory);
		
	}
	public List<Product> findByIfDisplayTrue(){
		return repository.findByDisplayTrue();
	}

	public Product findByProductName(String productName) {
		return repository.findByProductName(productName);
	}

}

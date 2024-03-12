package com.meghana.ecommerce.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meghana.ecommerce.dto.Items;
import com.meghana.ecommerce.repository.ItemRepository;

@Component
public class ItemDao {

	@Autowired
	ItemRepository repository;
	
	public Items findById(int id)
	{
		return repository.findById(id).orElse(null);
	}

	public void save(Items item) {
		repository.save(item);
		
	}

	public void remove(Items item) {
		repository.delete(item);
		
	}
}

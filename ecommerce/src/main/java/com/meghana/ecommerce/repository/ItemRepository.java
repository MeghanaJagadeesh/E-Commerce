package com.meghana.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meghana.ecommerce.dto.Items;

public interface ItemRepository extends JpaRepository<Items, Integer>{

}

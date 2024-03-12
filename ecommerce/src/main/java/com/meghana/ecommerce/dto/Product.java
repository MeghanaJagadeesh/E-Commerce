package com.meghana.ecommerce.dto;

import java.time.LocalDate;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Component
@Data
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String productName;
	private String productDescription;
	private double productPrice;
	private int productStock;
	private String productCategory;
	private LocalDate dateAdded;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private byte[] picture;
	private boolean display;

	public String generateBase64Image() { // this method is used to convert byte array into base64
		return Base64.encodeBase64String(this.getPicture());
	}

}

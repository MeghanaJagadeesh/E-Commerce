package com.meghana.ecommerce.dto;

import java.time.LocalDate;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

//we can't set the original product object to the customer because, original products will be having many 
//stocks and whenever customer modify the cart original product object wil get affected
//it is replica of original object
@Entity
@Data
public class Items {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int itemid;
	private String productName;
	private String productDescription;
	private double productPrice;
	private int productQuantity;
	private String productCategory;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private byte[] picture;

	private double amount;
	public String generateBase64Image() {
		return Base64.encodeBase64String(this.getPicture());
	}
}

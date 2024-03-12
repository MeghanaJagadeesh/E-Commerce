package com.meghana.ecommerce.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Component
@Entity
public class Customer {

	@Id
	@GeneratedValue(generator = "slno")
	@SequenceGenerator(initialValue = 101, allocationSize = 1, sequenceName = "slno", name = "slno")
	private int id;

	@NotEmpty(message = "*This should not be empty")
	@Size(min = 3, max = 20, message = "*character range should be 3 to 20")
	private String name;

	@DecimalMax(value = "12345678905", message = "*Enter proper mobile number")
	@DecimalMin(value = "123456", message = "*Enter proper mobile number")
	private long phoneNum;

	@NotEmpty(message = "*This should not be empty")
	@Email(message = "invalid email")
	private String email;

	@NotEmpty(message = "*This should not be empty")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "*password must be eight characters including one uppercase letter, one special character and alphanumeric characters")
	private String password;

	@NotNull(message = "Select One Date")
	@Past(message = "*Date of birth should be less tha current date")
	private LocalDate dob;

	@NotEmpty(message = "*This should not be empty")
	private String gender;

	int otp;
	boolean verify;
	boolean block;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	CustomerCart cart;
	

	

}

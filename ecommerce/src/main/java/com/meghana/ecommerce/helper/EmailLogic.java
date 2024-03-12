package com.meghana.ecommerce.helper;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.meghana.ecommerce.dto.Customer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailLogic {

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	Customer customer;

	public void sentOtp(Customer customer) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setFrom("meghanajagadeesh9686@gmail.com", "E-Commerce");
			helper.setTo(customer.getEmail());
			helper.setSubject("Verify OTP");
			helper.setText("<html><body><h2>Hello " + customer.getName() + "</h2><h3>Your one time password is: " + customer.getOtp()
					+ "</h3><h4>Thanks and Regards</h4></body></html>", true);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mailSender.send(message);
	}

}

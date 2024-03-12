package com.meghana.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.meghana.ecommerce.dto.Customer;
import com.meghana.ecommerce.service.CustomerService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommonController {

	@Autowired
	CustomerService customerService;

	@GetMapping("/")
	public String loadHome() {
		return "Home";
	}

	@GetMapping("/about-us")
	public String loadAboutUs() {
		return "AboutUs.html";
	}

	@GetMapping("/login")
	public String loadLogin() {
		return "login";
	}

	@PostMapping("/login")
	public String login(@RequestParam String emph, @RequestParam String password, ModelMap map, HttpSession session) {
		return customerService.login(emph, password, map, session);
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, ModelMap map) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			session.invalidate();
			map.put("pass", "Logout Sucessfully");
			return "Home";
		} else if (session.getAttribute("admin") != null) {
			session.invalidate();
			map.put("pass", "Logout Sucessfully");
			return "Home";
		} else {
			return loadHome();
		}
	}

	@GetMapping("/customer-viewproduct")
	public String customerViewProduct(ModelMap map, Customer customer) {
		customerService.viewProduct(map, customer);
		return "CustomerViewProduct";
	}
}

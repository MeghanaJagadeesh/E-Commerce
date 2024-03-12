package com.meghana.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.meghana.ecommerce.dto.Customer;
import com.meghana.ecommerce.service.CustomerService;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping("/customer")
@Controller
public class CustomerController {

	@Autowired
	Customer customer;

	@Autowired
	CustomerService customerService;

	@GetMapping("/process-signup")
	public String loadSignUp(ModelMap map) {
		map.put("customer", customer);
		return "SignUp.html";
	}

	@PostMapping("/process-signup")
	public String signUp(@Valid Customer customer, BindingResult result, ModelMap map) {
		if (result.hasErrors()) {
			return "SignUp.html";
		} else {
			return customerService.signUp(customer, map);
		}

	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam int otp, @RequestParam("id") int id, ModelMap map) {
		return customerService.verifyOtp(otp, id, map);
	}
	
	@PostMapping("/add-cart")
	public String addToCart(@RequestParam int inputValue, @RequestParam int id, HttpSession session, ModelMap map) {
		// we need to get the customer to check whether the customer has cart or not
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			return customerService.addToCrat(id, customer, map, session, inputValue);
		} else {
			map.put("fail", "Please Login!!");
			return "login";
		}
	}

	@GetMapping("/view-cart")
	public String viewCart(HttpSession session, ModelMap map) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			if (customer.isBlock()) {
				map.put("fail", "Customer has been blocked");
				return "CustomerViewProduct";
			} else {
				return customerService.viewCart(customer, session, map);
			}
		} else {
			map.put("fail", "Please Login!!");
			return "login";
		}
	}

	@GetMapping("/cart-remove/{itemid}")
	public String removeFromCart(@PathVariable int itemid, HttpSession session, ModelMap map) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			return customerService.removeFromCart(itemid, customer, map, session);
		} else {
			map.put("fail", "Please Login!!");
			return "login";
		}
	}

	@GetMapping("/buyall-products")
	public String buyAll(HttpSession session, ModelMap map) throws RazorpayException {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			return customerService.buyAll(customer, session, map);
		} else {
			map.put("fail", "Please Login!!");
			return "login";
		}
	}

	@GetMapping("/buy-product/{itemid}")
	public String buy(@PathVariable int itemid, HttpSession session, ModelMap map) throws RazorpayException {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			return customerService.buy(itemid, customer, session, map);
		} else {
			map.put("fail", "Please Login!!");
			return "login";
		}
	}

	@PostMapping("/payment-complete-allproduct/{id}")
	public String completeOrder(@PathVariable int id, HttpSession session, ModelMap map,
			@RequestParam String razorpay_payment_id) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			return customerService.completeOrder(id, razorpay_payment_id, customer, map);
		} else {
			map.put("fail", "Session Expired, Login Again");
			return "Home";
		}
	}

	@PostMapping("/payment-complete-product/{id}/{itemid}")
	public String completeOrderforSingle(@PathVariable int id, @PathVariable int itemid, HttpSession session,
			ModelMap map, @RequestParam String razorpay_payment_id) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			return customerService.completeOrder(id, itemid, razorpay_payment_id, customer, map, session);
		} else {
			map.put("fail", "Session Expired, Login Again");
			return "Home";
		}
	}

	@GetMapping("/view-orders")
	public String viewOrders(HttpSession session, ModelMap map) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			return customerService.viewOrders(customer, session, map);
		} else {
			map.put("fail", "Please Login");
			return "login";
		}
	}

	@GetMapping("/view-ordered-products/{id}")
	public String viewOrdersProducts(@PathVariable int id, HttpSession session, ModelMap map) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			return customerService.viewOrdersProducts(id, customer, session, map);
		} else {
			map.put("fail", "Please Login");
			return "Home";
		}
	}
}

package com.meghana.ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.meghana.ecommerce.dao.CustomerDao;
import com.meghana.ecommerce.dao.ItemDao;
import com.meghana.ecommerce.dao.OrderDetailsDao;
import com.meghana.ecommerce.dao.PaymentDao;
import com.meghana.ecommerce.dao.ProductDao;
import com.meghana.ecommerce.dto.Customer;
import com.meghana.ecommerce.dto.CustomerCart;
import com.meghana.ecommerce.dto.Items;
import com.meghana.ecommerce.dto.OrderDetails;
import com.meghana.ecommerce.dto.PaymentDetails;
import com.meghana.ecommerce.dto.Product;
import com.meghana.ecommerce.helper.AES;
import com.meghana.ecommerce.helper.EmailLogic;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;

@Component
public class CustomerService {

	@Autowired
	CustomerDao customerDao;

	@Autowired
	EmailLogic emailLogic;

	@Autowired
	ProductDao productDao;

	@Autowired
	ItemDao itemDao;

	@Autowired
	PaymentDao paymentDao;

	@Autowired
	OrderDetailsDao orderDetailsDao;

	@Autowired
	OrderDetails orderDetails;

	@Autowired
	PaymentDetails details;

	public String signUp(Customer customer, ModelMap map) {
		List<Customer> exCustomer = customerDao.findByEmailOrPhoneNum(customer.getEmail(), customer.getPhoneNum());
		if (!exCustomer.isEmpty()) {
			map.put("fail", "Account already exists");
			return "SignUp.html";
		} else {
			int otp = new Random().nextInt(1000000);

			customer.setOtp(otp);
			customer.setPassword(AES.encrypt(customer.getPassword(), "123"));
			customerDao.save(customer);
			emailLogic.sentOtp(customer); //since we need customer details to send otp we are passing its object
			map.put("id", customer.getId());
			return "enterOtp.html";
		}

	}

	public String verifyOtp(int otp, int id, ModelMap map) {
		Customer customer = customerDao.findById(id);
		if (customer.getOtp() == otp) {
			customer.setVerify(true);
			customerDao.update(customer);
			map.put("pass", "Account created successfully, You can login!!");
			return "login.html";
		} else {
			map.put("fail", "Invalid Otp, Try again!!");
			map.put("id", id);
			return "enterOtp.html";
		}

	}

	public String login(String emph, String password, ModelMap map, HttpSession session) {
		if (emph.equals("admin") && password.equals("admin")) {
			session.setAttribute("admin", "admin");
			map.put("pass", "Admin Login Sucessfully");
			return "AdminHome.html";
		} else {
			long mobile = 0;
			String email = null;

			try {
				mobile = Long.parseLong(email);
			} catch (NumberFormatException e) {
				email = emph;
			}
			List<Customer> cutomer = customerDao.findByEmailOrPhoneNum(email, mobile);
			if (cutomer.isEmpty()) {
				map.put("fail", "Invalid Email or Phonenumber");
				return "login.html";
			} else {
				Customer customer = cutomer.get(0);
				if (AES.decrypt(customer.getPassword(), "123").equals(password)) {
					if (customer.isVerify()) {
						if (customer.isBlock()) {
							map.put("fail", "Your Account Has Been Disabled");
							return "login";
						} else {
							map.put("pass", "Login Sucessfully!!!");
							session.setAttribute("customer", customer);
							return viewProduct(map, customer);
						}
					} else {
						int otp = new Random().nextInt(1000000);
						customer.setOtp(otp);
						customerDao.save(customer);
						emailLogic.sentOtp(customer);
						map.put("id", customer.getId());
						map.put("fail", "Verify your account");
						return "enterOtp.html";
					}
				} else {
					map.put("fail", "Invalid Password");
					return "login.html";
				}
			}

		}

	}

	public String viewProduct(ModelMap map, Customer customer) {
		List<Product> products = customerDao.findByIfDisplayTrue();
		if (products.isEmpty()) {
			map.put("fail", "No Products Found");
			return "CustomerViewProduct";
		} else {
			map.put("products", products);
			return "CustomerViewProduct";
		}

	}

	public String addToCrat(int id, Customer customer, ModelMap map, HttpSession session, int inputValue) {

		// checking cart
		Product product = productDao.findById(id);
		CustomerCart cart = customer.getCart();
		if (cart == null) {
			cart = new CustomerCart();
		}
		List<Items> cartItems = cart.getItem();
		if (cartItems == null) {
			cartItems = new ArrayList<Items>();
		}

		// checking product
		if (product.getProductStock() > 0) {
			// if product is already exist in cart
			boolean flag = true;
			for (Items item : cartItems) {
				if (item.getProductName().equals(product.getProductName())) {
					flag = false;
					item.setProductQuantity(item.getProductQuantity() + inputValue);
					item.setAmount(item.getAmount() + (product.getProductPrice() * inputValue));
					break;
				}
			}

			// if product is not there create new product

			if (flag) {
				// If item is new in cart
				Items item = new Items();

				item.setProductCategory(product.getProductCategory());
				item.setProductDescription(product.getProductDescription());
				item.setProductName(product.getProductName());
				item.setPicture(product.getPicture());
				item.setProductPrice(product.getProductPrice());
				item.setProductQuantity(inputValue);
				item.setAmount(product.getProductPrice() * inputValue);
				cartItems.add(item);

			}

			// storing object to cart

			cart.setId(customer.getId());
			cart.setItem(cartItems);
			cart.setTotalAmount(
					cart.getItem().stream().mapToDouble(x -> (x.getProductPrice() * x.getProductQuantity())).sum());
			customer.setCart(cart);
			customerDao.save(customer);

			session.setAttribute("customer", customer);
			map.put("pass", "Product Added to Cart");
			map.put("cart", cart);
			return "CustomerCart";
		} else {
			map.put("outofstock", "Product Out of stock");
			return viewProduct(map, customer);
		}
	}

	public String viewCart(Customer customer, HttpSession session, ModelMap map) {
		CustomerCart cart = customer.getCart();
		if (cart == null) {
			map.put("fail", "No Products are found in cart");
			return viewProduct(map, customer);
		} else {
			map.put("cart", cart);
			return "CustomerCart";
		}
	}

	public String removeFromCart(int id, Customer customer, ModelMap map, HttpSession session) {
		CustomerCart cart = customer.getCart();
		List<Items> items = cart.getItem();
		Items item = itemDao.findById(id);

		if (item != null) {
			if (item.getProductQuantity() > 1) {
				for (Items item2 : items) {
					if (item2.getProductName().equals(item.getProductName())) {
						item2.setProductQuantity(item2.getProductQuantity() - 1);
						item2.setAmount(item2.getAmount() - item2.getProductPrice());
					}
				}
			} else {
				items.remove(item);
			}
			cart.setItem(items);

			customer.setCart(cart);
			cart.setTotalAmount(
					cart.getItem().stream().mapToDouble(x -> ((x.getProductPrice()) * (x.getProductQuantity()))).sum());
			customerDao.save(customer);
			if (item != null && item.getProductQuantity() == 1) {
				itemDao.remove(item);
			}
			session.setAttribute("customer", customer);
			map.put("pass", "Product Removed from Cart");
			return viewCart(customer, session, map);
		} else {
			map.put("fail", "Item Not available");
			return "CustomerCart";
		}
	}

	public String buyAll(Customer customer, HttpSession session, ModelMap map) throws RazorpayException {
		RazorpayClient client = new RazorpayClient("rzp_test_v135J8LiXoRreU", "W2czuNW2bsnJsxu2LQnzYsrf");

		JSONObject object = new JSONObject();
		object.put("amount", customer.getCart().getTotalAmount() * 100);
		object.put("currency", "INR");

		Order order = client.orders.create(object);

		details.setAmount(customer.getCart().getTotalAmount());
		details.setCurrency(order.get("currency"));
		details.setDescription("Payment for item id" + customer.getCart().getItem().stream().map(x -> x.getItemid()));
		details.setImage(
				"https://www.shutterstock.com/image-vector/mobile-application-shopping-online-on-260nw-1379237159.jpg");
		details.setKeyCode("rzp_test_v135J8LiXoRreU");
		details.setName("Ecommerce Shopping");
		details.setOrder_id(order.get("id"));
		details.setStatus("created");

		paymentDao.save(details);

		map.put("details", details);
		map.put("customer", customer);
		return "PaymentPageAll";
	}

	public String buy(int id, Customer customer, HttpSession session, ModelMap map) throws RazorpayException {
		Items item = itemDao.findById(id);
		RazorpayClient client = new RazorpayClient("rzp_test_v135J8LiXoRreU", "W2czuNW2bsnJsxu2LQnzYsrf");

		JSONObject object = new JSONObject();
		object.put("amount", item.getAmount() * 100);
		object.put("currency", "INR");

		Order order = client.orders.create(object);

		details.setAmount(item.getAmount());
		details.setCurrency(order.get("currency"));
		details.setDescription("Payment for item id " + item.getItemid());
		details.setImage(
				"https://img.freepik.com/premium-vector/online-shopping-logo-design-vector-template_712837-74.jpg");
		details.setKeyCode("rzp_test_v135J8LiXoRreU");
		details.setName("Ecommerce Shopping");
		details.setOrder_id(order.get("id"));
		details.setStatus("created");

		paymentDao.save(details);

		map.put("item", item);
		map.put("details", details);
		map.put("customer", customer);
		return "PaymentPage";

	}

	public String completeOrder(int id, String razorpay_payment_id, Customer customer, ModelMap map) {
		PaymentDetails details = paymentDao.findById(id);
		details.setPayment_id(razorpay_payment_id);
		details.setStatus("success");
		paymentDao.save(details);

		// saving order history
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setAmount(details.getAmount());
		orderDetails.setOrder_id(details.getOrder_id());
		orderDetails.setCustomer(customer);
		orderDetails.setPayment_id(razorpay_payment_id);
		orderDetails.setDateTime(LocalDateTime.now());
		orderDetails.setItem(customer.getCart().getItem());

		orderDetailsDao.save(orderDetails);

		// reducing qty in database
		List<Items> list = customer.getCart().getItem();

		for (Items item : list) {
			Product product = productDao.findByProductName(item.getProductName());
			product.setProductStock(product.getProductStock() - item.getProductQuantity());
			productDao.save(product);
		}

		customer.getCart().setItem(null);
		customer.getCart().setTotalAmount(0);
		customerDao.save(customer);

		map.put("pass", "Payment Complete");
		return viewProduct(map, customer);
	}

	public String completeOrder(int id, int itemid, String razorpay_payment_id, Customer customer, ModelMap map,
			HttpSession session) {
		PaymentDetails details = paymentDao.findById(id);
		details.setPayment_id(razorpay_payment_id);
		details.setStatus("success");
		paymentDao.save(details);

		Items item = itemDao.findById(itemid);
		// saving order history
		orderDetails.setAmount(details.getAmount());
		orderDetails.setOrder_id(details.getOrder_id());
		orderDetails.setCustomer(customer);
		orderDetails.setPayment_id(razorpay_payment_id);
		orderDetails.setDateTime(LocalDateTime.now());
		List<Items> newList = new ArrayList<Items>();
		newList.add(item);
		orderDetails.setItem(newList);

		orderDetailsDao.save(orderDetails);

		// reduing stock from the database
		Product product = productDao.findByProductName(item.getProductName());
		product.setProductStock(product.getProductStock() - item.getProductQuantity());
		productDao.save(product);

		// removing item from cart
		Items removeItem = null;
		CustomerCart cart = customer.getCart();
		List<Items> list = customer.getCart().getItem();
		for (Items item2 : list) {
			if (item2.getProductName().equals(item.getProductName())) {
				removeItem = item2;
				break;
			}
		}
		cart.setTotalAmount(cart.getTotalAmount() - removeItem.getAmount());
		list.remove(removeItem);
		cart.setItem(list);

		// since we have modified the customer cart we are saving customer so tha cart
		// as well as items will get save
		customerDao.save(customer);

		map.put("pass", "Payment Complete");
		return viewProduct(map, customer);
	}

	public String viewOrders(Customer customer, HttpSession session, ModelMap map) {
		List<OrderDetails> orders = orderDetailsDao.fetchByCustomer(customer);
		if (orders.isEmpty()) {
			map.put("fail", "No Orders Placed Yet");
			return viewProduct(map, customer);
		} else {
			map.put("orders", orders);
			return "ViewOrders";
		}
	}

	public String viewOrdersProducts(int id, Customer customer, HttpSession session, ModelMap map) {
		OrderDetails order = orderDetailsDao.findById(id);
		map.put("order", order);
		return "ViewOrderItems";
	}

}

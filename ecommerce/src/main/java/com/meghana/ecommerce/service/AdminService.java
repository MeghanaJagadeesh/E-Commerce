package com.meghana.ecommerce.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import com.meghana.ecommerce.dao.CustomerDao;
import com.meghana.ecommerce.dao.ProductDao;
import com.meghana.ecommerce.dto.Customer;
import com.meghana.ecommerce.dto.Product;

import jakarta.servlet.http.HttpSession;

@Service
public class AdminService {

	@Autowired
	ProductDao dao;

	@Autowired
	CustomerDao customerDao;

	public String addProduct(Product product, MultipartFile productImage, HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			byte[] picture;
			try {
				picture = new byte[productImage.getInputStream().available()];
				productImage.getInputStream().read(picture);
			} catch (IOException e) {
				map.put("error", "Error occurs while uploading file, try again");
				return "AddProduct.html";
			}
			product.setPicture(picture);
			dao.save(product);

			map.put("pass", "Product Added Sucessfully");
			return "AddProduct.html";
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}

	public String fetchAllProduct(ModelMap map) {
		List<Product> products = dao.fetchAllProducts();
		if (products.isEmpty()) {
			map.put("fail", "No Products found");
			return "AdminHome.html";
		} else {
			map.put("products", products);
			return "AdminViewProducts";
		}
	}

	public String changeStatus(int id, ModelMap map) {
		Product product = dao.findById(id);
		if (product.isDisplay())
			product.setDisplay(false);
		else {
			product.setDisplay(true);
		}
		dao.save(product);
		map.put("pass", "Status changed sucessfully");
		return fetchAllProduct(map);
	}

	public String deleteProduct(int id, ModelMap map) {
		dao.delete(id);
		map.put("fail", "Product Deleted Sucessfully!!");
		return fetchAllProduct(map);
	}

	public String editProduct(int id, ModelMap map) {
		Product product = dao.findById(id);
		map.put("product", product);
		return "EditProduct";

	}

	public String updateProduct(Product product, MultipartFile productImage, HttpSession session, ModelMap map) {
		byte[] picture;
		try {
			picture = new byte[productImage.getInputStream().available()];
			productImage.getInputStream().read(picture);
		} catch (IOException e) {
			map.put("error", "Error occurs while uploading file, try again");
			return "AddProduct.html";
		}
		if (picture.length == 0)
			product.setPicture(dao.findById(product.getId()).getPicture());
		dao.save(product);
		map.put("pass", "Producted Updated Sucessfully");
		return fetchAllProduct(map);
	}

	public String fetchProduct(String productCategory, HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			List<Product> products = dao.fetchProduct(productCategory);
			if (products.isEmpty()) {
				map.put("fail", "No Product found");
				return "FetchProduct";
			} else {
				map.put("products", products);
				return "AdminViewProducts";
			}
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}

	}

	public String userManagement(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			List<Customer> customers = customerDao.findAll();
			if (!customers.isEmpty()) {
				map.put("customers", customers);
				return "CustomerManagement";
			} else {
				map.put("fail", "No Customers Found");
				return "AdminHome";
			}
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}

	}

	public String deleteCustomer(int id, HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			customerDao.delete(id);
			map.put("pass", "Customer Deleted");
			return userManagement(session, map);

		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}

	public String actionCheck(int id, HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			Customer customer = customerDao.findById(id);
			if(customer.isBlock())
			{
				customer.setBlock(false);
			}
			else {
				customer.setBlock(true);
			}
			customerDao.save(customer);
			map.put("pass", "Action Updated Sucessfully!!");
			return userManagement(session, map);
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	
	}

}

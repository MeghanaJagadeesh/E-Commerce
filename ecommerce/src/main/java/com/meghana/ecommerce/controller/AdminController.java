package com.meghana.ecommerce.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.meghana.ecommerce.dto.Product;
import com.meghana.ecommerce.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	Product product;

	@Autowired
	AdminService service;

	@GetMapping("/add-product")
	public String loadAddProduct(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return "AddProduct.html";
		} else {
			map.put("fail", "Session Expiry, Please Login!!");
			return "login.html";
		}
	}

	@GetMapping("/home")
	public String adminHome(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return "AdminHome.html";
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}

	@PostMapping("/add-product")
	public String addProduct(Product product, @RequestParam MultipartFile productImage, HttpSession session,
			ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return service.addProduct(product, productImage, session, map);
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return null;
		}
	}

	@GetMapping("/fetch-product")
	public String loadFetchProduct(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return "FetchProduct.html";
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}

	@PostMapping("/fetch-product")
	public String fetchProduct(@RequestParam String productCategory, HttpSession session, ModelMap map){
		if(session.getAttribute("admin")!=null)
		{
			return service.fetchProduct(productCategory, session, map);	
		}
		else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}

	@GetMapping("/fetchAll-products")
	public String fetchAll(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return service.fetchAllProduct(map);
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}

	@GetMapping("/change/{id}")
	public String changeStatus(@PathVariable int id, HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return service.changeStatus(id, map);
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return service.deleteProduct(id, map);
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}
	
	@GetMapping("/back")
	public String back(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return "AdminHome";
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editProduct(@PathVariable int id,HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return service.editProduct(id, map);		 
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}
	@PostMapping("/update-product")
	public String updateProduct(Product product, @RequestParam MultipartFile productImage, HttpSession session,
			ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return service.updateProduct(product, productImage, session, map);
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}
	
	@GetMapping("/user-management")
	public String userManagement(HttpSession session, ModelMap map)
	{
		if (session.getAttribute("admin") != null) {
			return service.userManagement(session,map);
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}
	
	@GetMapping("/delete-customer/{id}")
	public String deleteCustomer(@PathVariable int id, HttpSession session, ModelMap map)
	{
		if (session.getAttribute("admin") != null) {
			return service.deleteCustomer(id,session,map);
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}
	
	@GetMapping("/action-block-customer/{id}")
	public String actionCheck(@PathVariable int id, HttpSession session, ModelMap map)
	{
		if (session.getAttribute("admin") != null) {
			return service.actionCheck(id,session,map);
		} else {
			map.put("fail", "Session Expiry!! Please Login");
			return "login.html";
		}
	}
}

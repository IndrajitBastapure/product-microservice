package com.cisco.microservices.products;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/product")
public class ProductController {

	protected Logger logger = Logger.getLogger(ProductController.class.getName());

	@Autowired
	private ProductDAO productDao;

	@RequestMapping(value = "/addProductOld", method = RequestMethod.POST, consumes = "application/json")
	public Response addProductOld(@RequestBody Product product) {
		Response res = new Response();
		List<Product> list = new ArrayList<Product>();
		Product p = new Product();
		p.setProductName(product.getProductName());
		p.setDescription(product.getDescription());
		p.setAddedDate(new Date());
		Product addedProduct = productDao.save(p);

		list.add(addedProduct);

		res.setStatus("200");
		res.setDescription("Product added successfully!");
		res.setData(list);
		return res;
	}

	@RequestMapping(value = "/addProduct", method = RequestMethod.POST, consumes = "application/json")
	public Response addProduct(@RequestBody Product product) {
		String productName = product.getProductName();
		List<Product> productList = new ArrayList<Product>();
		Response res = new Response();
		logger.info("Product's addProduct() invoked for :" + productName);

		if (validateProductName(productName)) {
			res.setStatus("400");
			res.setDescription("Product already exist!");
			ProductError err = new ProductError();
			err.setMsg("Product already exist");
			err.setParam("productName");
			err.setValue(productName);
			res.setError(err);
			System.out.println("Product already exist");
		} else {

			try {
				Product p = new Product();
				p.setProductName(productName);
				p.setDescription(product.getDescription());
				Product addedProduct = productDao.save(p);
				logger.info("Product added!");
				productList.add(p);

				res.setStatus("200");
				res.setDescription("Product added successfully!");
				res.setData(productList);
			} catch (ConstraintViolationException e) {
				res.setStatus("400");
				res.setDescription("Server error!");
				ProductError err = new ProductError();
				err.setMsg("Something went wrong");
				err.setParam("productName");
				err.setValue(productName);
				res.setError(err);
			}
		}
		return res;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private Response list() {
		Response res = new Response();
		List<Product> productList = productDao.findAll();
		if (productList.size() <= 0) {
			res.setStatus("200");
			res.setDescription("OOPs! it seems there are no product's added yet!");
		} else {
			res.setStatus("200");
			res.setDescription("This is the product's list");
		}
		res.setData(productList);
		return res;
	}

	private boolean validateProductName(String productName) {
		List<Product> product = null;
		try {
			System.out.println(productName);
			product = productDao.findByProductName(productName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (product.size() <= 0) {
			return false;
		} else {
			return true;
		}
	}
}

package com.cisco.microservices.products;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/product")
public class ProductController {

	protected Logger logger = Logger.getLogger(ProductController.class.getName());
	
	@Autowired
	private ProductDAO productDao;

	@RequestMapping(value = "/addProduct", method = RequestMethod.POST)
	public Response addProduct() {
		Response res = new Response();
		Product p = new Product();
		List<Product> list = new ArrayList<Product>();
		
		p.setProductName("Amway");
		p.setDescription("Nutrilite");
		p.setAddedDate(new Date());
		
		Product addProduct = productDao.save(p);
		System.out.println(addProduct);

		res.setStatus("200");
		res.setDescription("Test description");
		res.setProductList(list);
		return res;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private @ResponseBody List<Product> list() {
		List<Product> productList = productDao.findAll();
		return productList;
	}
}

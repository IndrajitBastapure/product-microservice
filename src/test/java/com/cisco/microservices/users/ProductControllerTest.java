package com.cisco.microservices.users;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cisco.microservices.products.AppConfig;
import com.cisco.microservices.products.Product;

/**
 * @author roshankumarm
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class ProductControllerTest {

	private MockMvc mockMvc;

	private MediaType contentType = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays
				.asList(converters)
				.stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny().get();

		Assert.assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	/**
	 * 
	 * 
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws IOException, Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.build();
		mockMvc.perform(
				delete("/product/delete/TestAmway").contentType(MediaType.ALL))
				.andExpect(status().isOk());
	}

	/**
	 * add product test case.
	 * 
	 * @throws Exception
	 */
	@Test
	public void addProduct() throws Exception {
		Product product = new Product();
		product.setProductName("TestAmway");
		product.setDescription("Test Amway product");
		mockMvc.perform(
				post("/product/addProduct").content(this.json(product))
						.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.status", is("200")))
				.andExpect(
						jsonPath("$.description",
								is("Product added successfully")));

	}

	/**
	 * add product with blank name. Should generate error.
	 * 
	 * @throws Exception
	 */
	@Test
	public void addProductWithoutName() throws Exception {
		Product product = new Product();
		product.setProductName("");
		product.setDescription("Test Amway product");
		mockMvc.perform(
				post("/product/addProduct").content(this.json(product))
						.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.status", is("400")))
				.andExpect(jsonPath("$.description", is("Invalid productName")));

	}

	/**
	 * add Product Without Descrioption should not fail.
	 * 
	 * @throws Exception
	 */
	@Test
	public void addProductWithoutDescrioption() throws Exception {
		Product product = new Product();
		product.setProductName("TestAmway");
		product.setDescription("");
		mockMvc.perform(
				post("/product/addProduct").content(this.json(product))
						.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.status", is("200")))
				.andExpect(
						jsonPath("$.description",
								is("Product added successfully")));

	}

	/**
	 * add Product which already Exists should generate error.
	 * 
	 * @throws Exception
	 */
	@Test
	public void addProductAlreadyExists() throws Exception {
		Product product = new Product();
		product.setProductName("TestAmway");
		product.setDescription("");
		mockMvc.perform(
				post("/product/addProduct").content(this.json(product))
						.contentType(contentType)).andExpect(status().isOk());
		mockMvc.perform(
				post("/product/addProduct").content(this.json(product))
						.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.status", is("409")))
				.andExpect(
						jsonPath("$.description", is("Product already exist")));

	}

	/**
	 * 
	 * Update product use case.
	 * @throws Exception
	 */
	@Test
	public void updateProduct() throws Exception {
		Product product = new Product();
		product.setProductName("TestAmway");
		product.setDescription("Test Amway product");
		mockMvc.perform(
				post("/product/addProduct").content(this.json(product))
						.contentType(contentType)).andExpect(status().isOk());
		product.setDescription("Test Amway product Updated");
		mockMvc.perform(
				put("/product/updateProduct").content(this.json(product))
						.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("200")))
				.andExpect(
						jsonPath("$.description",
								is("Product Updated successfully")))
				.andExpect(
						jsonPath("$.data[0].description",
								is("Test Amway product Updated")));
	}

	/**
	 * 
	 * Update UnAvailable Product should not result json status 200.
	 * @throws Exception
	 */
	@Test
	public void updateUnAvailableProduct() throws Exception {
		Product product = new Product();
		product.setProductName("TestAmway1");// does not present
		product.setDescription("");
		mockMvc.perform(
				put("/product/updateProduct").content(this.json(product))
						.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("404")))
				.andExpect(
						jsonPath("$.description", is("Invalid product name")));
	}

	/**
	 * Delete Product with provided product name.
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteProduct() throws Exception {
		Product product = new Product();
		product.setProductName("TestAmway");
		product.setDescription("Test Amway product");
		mockMvc.perform(
				post("/product/addProduct").content(this.json(product))
						.contentType(contentType)).andExpect(status().isOk());
		mockMvc.perform(
				delete("/product/delete/TestAmway").contentType(MediaType.ALL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("200")))
				.andExpect(
						jsonPath("$.description",
								is("Product deleted successfully")));

	}

	/**
	 * 
	 * Delete non exist Product should Failed.
	 * @throws Exception
	 */
	@Test
	public void deleteProductFailed() throws Exception {
		mockMvc.perform(
				delete("/product/delete/TestAmwayTest").contentType(
						MediaType.ALL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("404")))
				.andExpect(
						jsonPath("$.description", is("Invalid product name")));

	}

	/**
	 * 
	 * List all the product.
	 * @throws Exception
	 */
	@Test
	public void listProduct() throws Exception {
		Product product = new Product();
		product.setProductName("TestAmway");
		product.setDescription("Test Amway product");
		mockMvc.perform(
				post("/product/addProduct").content(this.json(product))
						.contentType(contentType)).andExpect(status().isOk());
		mockMvc.perform(get("/product/list").contentType(MediaType.ALL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("200")))
				.andExpect(
						jsonPath("$.description",
								is("This is the product's list")))
				.andExpect(jsonPath("$.error").value(IsNull.nullValue()));
	}

	/**
	 * Configure set up which will run after each test.
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	@After
	public void tearDown() throws IOException, Exception {
		/*
		 * mockMvc.perform(
		 * delete("/user/delete/mockuser").contentType(MediaType.ALL))
		 * .andExpect(status().isOk());
		 */
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o,
				MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}

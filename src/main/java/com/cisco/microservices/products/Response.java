package com.cisco.microservices.products;

import java.util.List;

/**
 * Response to the REST API's request with its attributes  
 * 
 * @author Sandip Bastapure
 */
public class Response {
	private String status;

	private String description;

	private List<Product> data;

	private ProductError error;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Product> getData() {
		return data;
	}

	public void setData(List<Product> data) {
		this.data = data;
	}

	public ProductError getError() {
		return error;
	}

	public void setError(ProductError error) {
		this.error = error;
	}

}

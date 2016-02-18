package com.cisco.microservices.products;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ProductDAO extends JpaRepository<Product, Long> {

	public Product findByDescription(String email);

	public List<Product> findAll();
	
	public List<Product> findByProductName(String productName);
}

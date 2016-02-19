package com.cisco.microservices.products;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ProductDAO extends JpaRepository<Product, Long> {

	public Product findByDescription(String email);

	public List<Product> findAll();
	
	public List<Product> findByProductName(String productName);
	
	public List<Product> deleteByProductName(String productName);
	
	public int deleteById(Long productId);
	
	
    @Modifying
    @Query("UPDATE Product p SET p.description = :description, added_date= :addedDate WHERE p.productName = :productName")
    public int updateProduct(
    		@Param("description") String description,
    		@Param("addedDate") Date addedDate, 
    		@Param("productName") String productName);
	

}

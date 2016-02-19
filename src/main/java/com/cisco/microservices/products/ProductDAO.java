package com.cisco.microservices.products;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Product Repository to perform product CRUD operations using JpaRepository.
 * {@link JpaRepository}.
 * 
 * @author Sandip Bastapure
 */
@Transactional
@Repository
public interface ProductDAO extends JpaRepository<Product, Long> {

	/**
	 * @Modifying creating own update query to update the products using JpaRepository
	 * 
	 * @param args
	 * 		NA
	 */
	public Product findByDescription(String email);

	public List<Product> findAll();
	
	public List<Product> findByProductName(String productName);
	
	public List<Product> deleteByProductName(String productName);
	
	public int deleteById(Long productId);
	
	/**
	 * @Modifying creating own update query to update the products using JpaRepository
	 * 
	 * @param args
	 * 		NA
	 */
	
    @Modifying
    @Query("UPDATE Product p SET p.description = :description, added_date= :addedDate WHERE p.productName = :productName")
    public int updateProduct(
    		@Param("description") String description,
    		@Param("addedDate") Date addedDate, 
    		@Param("productName") String productName);
	

}

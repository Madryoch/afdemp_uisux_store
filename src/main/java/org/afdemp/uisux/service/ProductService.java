package org.afdemp.uisux.service;

import java.util.ArrayList;
import java.util.List;

import org.afdemp.uisux.domain.Category;
import org.afdemp.uisux.domain.Product;

public interface ProductService {

	List<Product> findAll();
	
	Product findOne(Long id);
	
	ArrayList<Product> searchActive(String name);
	
	void removeOne(Long id);

	Product createProduct(Product product, String type);
	
	boolean restock(Long productId, int qty);
	
	void toggleActive(Product product);

	void deactivate(Long id);
	
	void activate(Long id);

	List<Product> findByCategory(Category category);
	
	List<Product> findAllActive();

	List<Product> findByCategoryAndActiveTrue(Category category);
}

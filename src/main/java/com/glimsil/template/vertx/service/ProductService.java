package com.glimsil.template.vertx.service;

import com.glimsil.template.vertx.api.dto.ProductDto;

public class ProductService {
	private static ProductService instance = null;
	public static synchronized ProductService getInstance() {
		if(null == instance) {
			instance = new ProductService();
		}
		return instance;
	}

	private ProductDto productDto = new ProductDto();

	private ProductService() {
		productDto.setId("123");
		productDto.setName("Product sample");
	}


	
	public String getHelloWorld() {
		return "Hello World!";
	}

	public ProductDto saveProduct(String id, String name) {
		productDto.setId(id);
		productDto.setName(name);
		return productDto;
	}

	public ProductDto getProduct() {
		return productDto;
	}

}

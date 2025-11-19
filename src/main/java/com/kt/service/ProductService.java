package com.kt.service;

public interface ProductService {

	void create(String name, Long price, Long stock);

	void update(Long productId, String name, Long price, Long stock);

	void delete(Long productId);
}

package com.kt.service;

import java.util.UUID;

public interface ProductService {

	void create(String name, Long price, Long stock);

	void update(UUID productId, String name, Long price, Long stock);

	void delete(UUID productId);
}

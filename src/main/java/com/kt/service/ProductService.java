package com.kt.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kt.constant.searchtype.ProductSearchType;
import com.kt.domain.dto.response.ProductResponse;
import com.kt.domain.entity.ProductEntity;

public interface ProductService {

	void create(String name, Long price, Long stock);

	void update(UUID productId, String name, Long price, Long stock);

	void delete(UUID productId);

	Page<ProductResponse.Search> search(Pageable pageable, String keyword, ProductSearchType type);

	ProductEntity detail(UUID productId);
}

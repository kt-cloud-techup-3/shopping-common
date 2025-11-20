package com.kt.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kt.constant.searchtype.ProductSearchType;
import com.kt.domain.dto.response.ProductResponse;

public interface ProductQueryDslRepository {
	Page<ProductResponse.Search> search(Pageable pageable, String keyword, ProductSearchType type);
}

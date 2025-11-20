package com.kt.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.constant.searchtype.ProductSearchType;
import com.kt.domain.dto.response.ProductResponse;
import com.kt.domain.entity.ProductEntity;
import com.kt.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Override
	public void create(
		String name,
		Long price,
		Long stock
	) {
		ProductEntity product = ProductEntity.create(name, price, stock);
		productRepository.save(product);
	}

	@Override
	public void update(
		UUID productId,
		String name,
		Long price,
		Long stock
	) {
		ProductEntity product = productRepository.findByIdOrThrow(productId);
		product.update(name, price, stock);
	}

	@Override
	public void delete(UUID productId) {
		ProductEntity product = productRepository.findByIdOrThrow(productId);
		product.delete();
	}

	@Override
	public Page<ProductResponse.Search> search(Pageable pageable, String keyword, ProductSearchType type) {
		return productRepository.search(pageable, keyword, type);
	}

	@Override
	public ProductEntity detail(UUID productId) {
		ProductEntity product = productRepository.findByIdOrThrow(productId);
		return product;
	}

}

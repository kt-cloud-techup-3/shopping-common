package com.kt.service;

import org.springframework.stereotype.Service;

import com.kt.domain.entity.ProductEntity;
import com.kt.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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
		Long productId,
		String name,
		Long price,
		Long stock
	) {
		ProductEntity product = productRepository.findByIdOrThrow(productId);
		product.update(name, price, stock);
	}

	@Override
	public void delete(Long productId) {
		ProductEntity product = productRepository.findByIdOrThrow(productId);
		product.delete();
	}
}

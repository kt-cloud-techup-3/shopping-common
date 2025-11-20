package com.kt.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.kt.constant.ProductStatus;
import com.kt.domain.dto.request.ProductRequest;
import com.kt.domain.dto.response.ProductResponse;
import com.kt.domain.entity.ProductEntity;
import com.kt.repository.ProductRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceTest {

	private final ProductService productService;

	private final ProductRepository productRepository;

	@Autowired
	ProductServiceTest(ProductService productService, ProductRepository productRepository) {
		this.productService = productService;
		this.productRepository = productRepository;
	}

	@BeforeEach
	void tearDown() {
		productRepository.deleteAll();
	}

	@Test
	void 상품_생성() {
		// given
		String productName = "상품1";
		long productPrice = 1000L;
		ProductRequest.Create request = new ProductRequest.Create(
			productName,
			productPrice,
			10L
		);

		// when
		productService.create(request.name(), request.price(), request.stock());

		// then
		ProductEntity product = productRepository.findAll()
			.stream()
			.filter(it -> it.getName().equals(productName))
			.findFirst()
			.orElseThrow();

		assertThat(product.getPrice()).isEqualTo(productPrice);
	}

	@Test
	void 삼품_수정() {
		// given
		ProductEntity product = ProductEntity.create(
			"상품1",
			1000L,
			10L
		);

		productRepository.save(product);

		// when
		ProductRequest.Update request = new ProductRequest.Update(
			"수정된상품명",
			2000L,
			20L
		);

		productService.update(
			product.getId(),
			request.name(),
			request.price(),
			request.stock()
		);

		// then
		ProductEntity foundProduct = productRepository.findByIdOrThrow(product.getId());
		assertThat(foundProduct.getName()).isEqualTo(request.name());
	}

	@Test
	void 상품_삭제() {
		// given
		ProductEntity product = ProductEntity.create(
			"상품1",
			1000L,
			10L
		);
		productRepository.save(product);

		// when
		productService.delete(product.getId());

		// then
		ProductEntity foundProduct = productRepository.findByIdOrThrow(product.getId());
		assertThat(foundProduct.getStatus()).isEqualTo(ProductStatus.DELETED);
	}

	@Test
	void 상품_조회() {
		// given
		List<ProductEntity> products = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			ProductEntity product = ProductEntity.create(
				"상품" + i,
				1000L,
				10L
			);
			products.add(product);
		}
		productRepository.saveAll(products);

		// when
		PageRequest pageRequest = PageRequest.of(1, 10);
		Page<ProductResponse.Search> search = productService.search(pageRequest, null, null);

		// then
		assertThat(search.getTotalElements()).isEqualTo(20);
		assertThat(search.getTotalPages()).isEqualTo(2);
		assertThat(search.getContent().size()).isEqualTo(10);
	}

	@Test
	void 상품_상세_조회() {
		// given
		ProductEntity product = ProductEntity.create(
			"상품1",
			1000L,
			10L
		);
		productRepository.save(product);

		// when
		ProductEntity foundProduct = productService.detail(product.getId());

		// then
		assertThat(foundProduct.getName()).isEqualTo(product.getName());
	}
}
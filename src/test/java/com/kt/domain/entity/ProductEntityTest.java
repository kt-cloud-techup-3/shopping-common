package com.kt.domain.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.kt.constant.ProductStatus;

@ActiveProfiles("test")
class ProductEntityTest {

	@Test
	void 객체생성_성공(){
		ProductEntity comparisonProduct = ProductEntity.create(
			"테스트상품명",
			1000L,
			5L,
			ProductStatus.ACTIVATED
		);

		ProductEntity subjectProduct = ProductEntity.create(
			"테스트상품명",
			1000L,
			5L,
			ProductStatus.ACTIVATED
		);

		assertThat(subjectProduct).isNotNull();

		assertThat(subjectProduct)
			.usingRecursiveComparison()
			.ignoringFields("detail")
			.isEqualTo(comparisonProduct);
	}
}
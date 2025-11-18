package com.kt.domain.entity;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.kt.domain.constant.ProductStatus;

@ActiveProfiles("test")
class ProductEntityTest {

	@Test
	void 객체생성_성공(){
		ProductEntity productEntity = ProductEntity.create(
			"테스트상품명",
			1000L,
			5L,
			ProductStatus.ACTIVATED
		);
		assertThat(productEntity.getName()).isEqualTo("테스트상품명");
		assertThat(productEntity.getPrice()).isEqualTo(1000L);
		assertThat(productEntity.getStock()).isEqualTo(5L);
		assertThat(productEntity.getStatus()).isEqualTo(ProductStatus.ACTIVATED);
	}

}
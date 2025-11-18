package com.kt.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiverVO {

	private String name;
	private String mobile;
	private String city;
	private String district;
	private String road_address;
	private String detail;
}

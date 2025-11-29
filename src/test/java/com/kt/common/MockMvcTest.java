package com.kt.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
public abstract class MockMvcTest {

	@Autowired
	public MockMvc mockMvc;

	@Autowired
	public ObjectMapper objectMapper;
}

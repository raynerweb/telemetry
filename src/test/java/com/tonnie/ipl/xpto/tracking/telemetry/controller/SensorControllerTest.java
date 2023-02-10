package com.tonnie.ipl.xpto.tracking.telemetry.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.tonnie.ipl.xpto.tracking.telemetry.XptoTrackingTelemetryMsApplication;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.CreateSensorResponseDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = XptoTrackingTelemetryMsApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class SensorControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;
	
	public static final String BASE_PATH = "/tracking";
	public static final String ENTITY_PATH = "/sensors";
	public static final String BASE_RESOURCE_REQUEST_PATH = "samples/request/";
	public static final String BASE_RESOURCE_RESPONSE_PATH = "samples/response/";
	
	public static final String SENSOR_REF_ID = "f8d6cede-e3dc-47c9-a61d-429f316c35cd";
	
	@Test
	@Order(1)
	void listEntities() throws Exception {
		URL responseResource = Resources.getResource(BASE_RESOURCE_RESPONSE_PATH+"response_list_all_sensors.json");
		mockMvc
			.perform(
				get(URI.create(BASE_PATH+ENTITY_PATH))
					.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
			.andExpect(status().isOk())
			.andExpect(content().json(Resources.toString(responseResource, Charset.defaultCharset())));
	}
	@Test
	@Order(2)
	void getEntity() throws Exception {
		URL responseResource = Resources.getResource(BASE_RESOURCE_RESPONSE_PATH+"response_get_sensor.json");
		mockMvc
		.perform(
				get(BASE_PATH+ENTITY_PATH+"/{sensorId}",SENSOR_REF_ID)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(content().json(Resources.toString(responseResource, Charset.defaultCharset())));
	}
	@Test
	@Order(3)
	void createEntity() throws Exception {
		URL requestResource = Resources.getResource(BASE_RESOURCE_REQUEST_PATH+"request_create_sensor.json");
		MvcResult mvcResult = mockMvc
		.perform(
				post(BASE_PATH+ENTITY_PATH)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(Resources.toString(requestResource, Charset.defaultCharset()))
				)
		.andExpect(status().isCreated()).andReturn();
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		Assertions.assertThat(jsonResponse).isNotEmpty();
		CreateSensorResponseDto responserDTO = jsonMapper.readValue(jsonResponse, CreateSensorResponseDto.class);
		Assertions.assertThat(responserDTO).isNotNull();
		Assertions.assertThat(responserDTO.getSensorId()).isNotNull();
	}
	@Test
	@Order(4)
	void updateEntity() throws Exception {
		URL requestResource = Resources.getResource(BASE_RESOURCE_REQUEST_PATH+"request_update_sensor.json");
		mockMvc
		.perform(
				put(BASE_PATH+ENTITY_PATH+"/{sensorId}",SENSOR_REF_ID)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(Resources.toString(requestResource, Charset.defaultCharset()))
				)
		.andExpect(status().isNoContent());
	}
	@Test
	@Order(5)
	void updateEntityFailsNotFound() throws Exception {
		URL requestResource = Resources.getResource(BASE_RESOURCE_REQUEST_PATH+"request_update_sensor.json");
		mockMvc
		.perform(
				put(BASE_PATH+ENTITY_PATH+"/{sensorId}",UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(Resources.toString(requestResource, Charset.defaultCharset()))
				)
		.andExpect(status().isNotFound());
	}
	@Test
	@Order(6)
	void deleteEntity() throws Exception {
		 mockMvc
		.perform(
				delete(BASE_PATH+ENTITY_PATH+"/{sensorId}",SENSOR_REF_ID)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNoContent());
		
		
	}
	@Test
	@Order(7)
	void deleteEntityFailsNotFound() throws Exception {
		 mockMvc
		.perform(
				delete(BASE_PATH+ENTITY_PATH+"/{sensorId}",UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNotFound());
		
		
	}
	@Test
	@Order(8)
	void getEntityFailsNotFound() throws Exception {
		mockMvc
		.perform(
				get(BASE_PATH+ENTITY_PATH+"/{sensorId}",UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNotFound());
	}
}
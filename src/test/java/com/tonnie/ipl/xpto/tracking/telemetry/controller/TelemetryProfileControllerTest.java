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
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.CreateTelemetryProfileRequestDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.CreateTelemetryProfileResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.UpdateTelemetryProfileRequestDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = XptoTrackingTelemetryMsApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class TelemetryProfileControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;
	
	public static final String BASE_PATH = "/tracking";
	public static final String ENTITY_PATH = "/telemetryprofiles";
	public static final String BASE_RESOURCE_REQUEST_PATH = "samples/request/";
	public static final String BASE_RESOURCE_RESPONSE_PATH = "samples/response/";
	
	public static final String SENSOR_REF_ID = "e8d6cede-e3dc-47c9-a61d-429f316c35cd";
	
	@Test
	@Order(1)
	void listEntities() throws Exception {
		URL responseResource = Resources.getResource(BASE_RESOURCE_RESPONSE_PATH+"response_list_all_telemetry_profile.json");
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
		URL responseResource = Resources.getResource(BASE_RESOURCE_RESPONSE_PATH+"response_get_telemetry_profile.json");
		mockMvc
		.perform(
				get(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}","e8d6cede-e3dc-47c9-a61d-429f316c35cf")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(content().json(Resources.toString(responseResource, Charset.defaultCharset())));
	}
	@Test
	@Order(3)
	void createEntity() throws Exception {
		URL requestResource = Resources.getResource(BASE_RESOURCE_REQUEST_PATH+"request_create_telemetry_profile.json");
		MvcResult mvcResult = mockMvc
		.perform(
				post(BASE_PATH+ENTITY_PATH)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(Resources.toString(requestResource, Charset.defaultCharset()))
				)
		.andExpect(status().isCreated()).andReturn();
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		Assertions.assertThat(jsonResponse).isNotEmpty();
		CreateTelemetryProfileResponseDto responserDTO = jsonMapper.readValue(jsonResponse, CreateTelemetryProfileResponseDto.class);
		Assertions.assertThat(responserDTO).isNotNull();
		Assertions.assertThat(responserDTO.getTelemetryprofileId()).isNotNull();
	}
	@Test
	@Order(3)
	void createEntityNoSensors() throws Exception {
		URL requestResource = Resources.getResource(BASE_RESOURCE_REQUEST_PATH+"request_create_telemetry_profile.json");
		CreateTelemetryProfileRequestDto requestDTO = jsonMapper.readValue(Resources.toString(requestResource, Charset.defaultCharset()), CreateTelemetryProfileRequestDto.class);
		requestDTO.setSensors(null);
		MvcResult mvcResult = mockMvc
		.perform(
				post(BASE_PATH+ENTITY_PATH)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(requestDTO))
				)
		.andExpect(status().isCreated()).andReturn();
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		Assertions.assertThat(jsonResponse).isNotEmpty();
		CreateTelemetryProfileResponseDto responserDTO = jsonMapper.readValue(jsonResponse, CreateTelemetryProfileResponseDto.class);
		Assertions.assertThat(responserDTO).isNotNull();
		Assertions.assertThat(responserDTO.getTelemetryprofileId()).isNotNull();
	}
	@Test
	@Order(3)
	void createEntityFailsSensorNotFould() throws Exception {
		URL requestResource = Resources.getResource(BASE_RESOURCE_REQUEST_PATH+"request_create_telemetry_profile.json");
		CreateTelemetryProfileRequestDto requestDTO = jsonMapper.readValue(Resources.toString(requestResource, Charset.defaultCharset()), CreateTelemetryProfileRequestDto.class);
		requestDTO.getSensors().clear();
		requestDTO.getSensors().add(UUID.randomUUID().toString());
		mockMvc
				.perform(
						post(BASE_PATH+ENTITY_PATH)
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonMapper.writeValueAsString(requestDTO))
						)
				.andExpect(status().isNotFound()).andReturn();
	}
	@Test
	@Order(4)
	void updateEntity() throws Exception {
		URL requestResource = Resources.getResource(BASE_RESOURCE_REQUEST_PATH+"request_update_telemetry_profile.json");
		mockMvc
		.perform(
				put(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}","e8d6cede-e3dc-47c9-a61d-429f316c35ce")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(Resources.toString(requestResource, Charset.defaultCharset()))
				)
		.andExpect(status().isNoContent()).andReturn();
	}
	@Test
	@Order(4)
	void updateEntityFailsTelemtryProfileNotFound() throws Exception {
		URL requestResource = Resources.getResource(BASE_RESOURCE_REQUEST_PATH+"request_update_telemetry_profile.json");
		mockMvc
		.perform(
				put(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}",UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(Resources.toString(requestResource, Charset.defaultCharset()))
				)
		.andExpect(status().isNotFound()).andReturn();
	}
	@Test
	@Order(4)
	void updateEntityFailsSensorNotFound() throws Exception {
		URL requestResource = Resources.getResource(BASE_RESOURCE_REQUEST_PATH+"request_update_telemetry_profile.json");
		UpdateTelemetryProfileRequestDto requestDTO = jsonMapper.readValue(Resources.toString(requestResource, Charset.defaultCharset()), UpdateTelemetryProfileRequestDto.class);
		requestDTO.getSensors().clear();
		requestDTO.getSensors().add(UUID.randomUUID().toString());
		mockMvc
		.perform(
				put(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}","e8d6cede-e3dc-47c9-a61d-429f316c35ce")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(requestDTO))
				)
		.andExpect(status().isNotFound()).andReturn();
	}
	@Test
	@Order(5)
	void addSensorToTelemetryProvides() throws Exception {
		mockMvc
		.perform(
				put(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}/sensor/{sensorId}","e8d6cede-e3dc-47c9-a61d-429f316c35ce","f8d6cede-e3dc-47c9-a61d-429f316c35ce")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNoContent()).andReturn();
	}
	@Test
	@Order(6)
	void addSensorToTelemetryProvidesFailsDuplicatedSensor() throws Exception {
		mockMvc
		.perform(
				put(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}/sensor/{sensorId}","e8d6cede-e3dc-47c9-a61d-429f316c35ce","f8d6cede-e3dc-47c9-a61d-429f316c35ce")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isConflict()).andReturn();
	}
	@Test
	@Order(7)
	void addSensorToTelemetryProvidesFailsTelemtryProfileNotFound() throws Exception {
		mockMvc
		.perform(
				put(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}/sensor/{sensorId}",UUID.randomUUID(),"f8d6cede-e3dc-47c9-a61d-429f316c35ce")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNotFound()).andReturn();
	}
	@Test
	@Order(8)
	void addSensorToTelemetryProvidesFailsSensorNotFound() throws Exception {
		mockMvc
		.perform(
				put(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}/sensor/{sensorId}","e8d6cede-e3dc-47c9-a61d-429f316c35ce",UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNotFound()).andReturn();
	}
	@Test
	@Order(9)
	void removeSensorFromTelemetryProvides() throws Exception {
		mockMvc
		.perform(
				delete(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}/sensor/{sensorId}","e8d6cede-e3dc-47c9-a61d-429f316c35ce","f8d6cede-e3dc-47c9-a61d-429f316c35ce")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNoContent()).andReturn();
	}
	@Test
	@Order(10)
	void removeSensorFromTelemetryProvidesFailsSensorNotFound() throws Exception {
		mockMvc
		.perform(
				delete(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}/sensor/{sensorId}","e8d6cede-e3dc-47c9-a61d-429f316c35ce",UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNotFound()).andReturn();
	}
	@Test
	@Order(10)
	void removeSensorFromTelemetryProvidesFailsTelemetryProvidesNotFound() throws Exception {
		mockMvc
		.perform(
				delete(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}/sensor/{sensorId}",UUID.randomUUID(),"f8d6cede-e3dc-47c9-a61d-429f316c35ce")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNotFound()).andReturn();
	}
	@Test
	@Order(10)
	void removeSensorFromTelemetryProvidesFailSensorNotAssociated() throws Exception {
		mockMvc
		.perform(
				delete(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}/sensor/{sensorId}","e8d6cede-e3dc-47c9-a61d-429f316c35cd","f8d6cede-e3dc-47c9-a61d-429f316c35cf")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNotFound()).andReturn();
	}
	
	@Test
	@Order(11)
	void deleteEntity() throws Exception {
		 mockMvc
		.perform(
				delete(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}","e8d6cede-e3dc-47c9-a61d-429f316c35ce")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNoContent()).andReturn();
		
		
	}
	@Test
	@Order(12)
	void deleteEntityFailsNotFound() throws Exception {
		mockMvc
		.perform(
				delete(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}",UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNotFound()).andReturn();
		
		
	}
	@Test
	@Order(13)
	void getEntityFailsNotFound() throws Exception {
		mockMvc
		.perform(
				get(BASE_PATH+ENTITY_PATH+"/{telemetryProfileId}",UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNotFound()).andReturn();
	}
}
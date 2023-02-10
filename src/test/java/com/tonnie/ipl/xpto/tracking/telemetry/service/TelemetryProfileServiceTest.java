package com.tonnie.ipl.xpto.tracking.telemetry.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.tonnie.ipl.xpto.tracking.telemetry.XptoTrackingTelemetryMsApplication;
import com.tonnie.ipl.xpto.tracking.telemetry.model.Sensor;
import com.tonnie.ipl.xpto.tracking.telemetry.model.TelemetryProfile;
import com.tonnie.ipl.xpto.tracking.telemetry.repository.SensorRepository;
import com.tonnie.ipl.xpto.tracking.telemetry.service.impl.TelemetryProfileService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = XptoTrackingTelemetryMsApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TelemetryProfileServiceTest {

	@Autowired
	private TelemetryProfileService service;
	
	@Autowired
	private SensorRepository sensorRepository;

	private TelemetryProfile createTestEntity() {

		Set<Sensor> sensors = new HashSet<>(sensorRepository.findAll());

		return TelemetryProfile.builder().name("TestCreateTlmProfile_" + Math.random()).sensors(sensors).build();
	}
	
	@Test
	@Order(1)
	@Transactional
	void findAll() {

		Assertions.assertThat(service.findAll()).hasSize(3);

	}

	@Test
	@Order(2)
	@Rollback(value = false)
	void save() {

		TelemetryProfile entity = service.save(createTestEntity());

		Assertions.assertThat(entity.getId()).isNotNull();

	}

	@Test
	@Order(3)
	@Transactional
	void findById() {

		TelemetryProfile entityRef = service.save(createTestEntity());

		Optional<TelemetryProfile> entityReturned = service.findById(entityRef.getId());

		Assertions.assertThat(entityReturned)
		.isPresent()
		.contains(entityRef);

	}

	@Test
	@Order(4)
	void findByIdFails() {

		Optional<TelemetryProfile> entityReturned = service.findById(UUID.randomUUID());

		Assertions.assertThat(entityReturned).isNotPresent();

	}

	@Test
	@Order(5)
	@Transactional
	void findByEntityId() {

		TelemetryProfile entityRef = service.save(createTestEntity());

		Optional<TelemetryProfile> entityReturned = service.findById(entityRef);

		Assertions.assertThat(entityReturned)
		.isPresent()
		.contains(entityRef);

	}

	@Test
	@Order(6)
	void findByEntityIdFails() {

		TelemetryProfile entityRef = createTestEntity(); 
		
		entityRef.setId(UUID.randomUUID());	

		Optional<TelemetryProfile> entityReturned = service.findById(entityRef);

		Assertions.assertThat(entityReturned).isNotPresent();

	}

	@Test
	@Order(7)
	@Rollback(value = false)
	void update() {

		TelemetryProfile entityRef = service.save(createTestEntity());

		entityRef.setName("NewTestCreateTlmProfile_" + Double.valueOf(Math.random()).intValue());

		TelemetryProfile entityUpdated = service.save(entityRef);

		Assertions.assertThat(entityUpdated.getName()).startsWith("NewTestCreateTlmProfile_");

	}

	@Test
	@Order(8)
	@Rollback(value = false)
	void delete() {

		TelemetryProfile entityRef = service.save(createTestEntity());

		UUID entifyRefId = entityRef.getId();

		Assertions.assertThat(entifyRefId).isNotNull();

		service.delete(entityRef);

		Optional<TelemetryProfile> entityDeleted = service.findById(entifyRefId);

		Assertions.assertThat(entityDeleted).isNotPresent();
	}

	@Test
	@Order(9)
	@Rollback(value = false)
	void deleteById() {

		TelemetryProfile entityRef = service.save(createTestEntity());

		UUID entifyRefId = entityRef.getId();

		Assertions.assertThat(entifyRefId).isNotNull();

		service.delete(entifyRefId);

		Optional<TelemetryProfile> entityDeleted = service.findById(entifyRefId);

		Assertions.assertThat(entityDeleted).isNotPresent();
	}

}

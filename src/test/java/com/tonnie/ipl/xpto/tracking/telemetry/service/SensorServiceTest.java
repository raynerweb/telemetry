package com.tonnie.ipl.xpto.tracking.telemetry.service;

import java.util.Optional;
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

import com.tonnie.ipl.xpto.tracking.telemetry.XptoTrackingTelemetryMsApplication;
import com.tonnie.ipl.xpto.tracking.telemetry.enums.SensorType;
import com.tonnie.ipl.xpto.tracking.telemetry.model.Sensor;
import com.tonnie.ipl.xpto.tracking.telemetry.service.impl.SensorService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = XptoTrackingTelemetryMsApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SensorServiceTest {

	@Autowired
	private SensorService service;

	private Sensor createTestEntity() {
		return Sensor.builder()
					.maxValue(390.0d)
					.minValue(0.0d)
					.name("TestCreateSensor_" + Math.random())
					.type(SensorType.SPEED)
					.unit("Km/h").build();
	}
	
	
	@Test
	@Order(1)
	void findAll() {

		Assertions.assertThat(service.findAll()).hasSize(3);

	}

	@Test
	@Order(2)
	@Rollback(value = false)
	void save() {

		Sensor entity = service.save(createTestEntity());

		Assertions.assertThat(entity.getId()).isNotNull();

	}

	@Test
	@Order(3)
	void findById() {

		Sensor entityRef = service.save(createTestEntity());

		Optional<Sensor> entityReturned = service.findById(entityRef.getId());

		Assertions.assertThat(entityReturned)
		.isPresent()
		.contains(entityRef);

	}

	@Test
	@Order(4)
	void findByIdFails() {

		Optional<Sensor> entityReturned = service.findById(UUID.randomUUID());

		Assertions.assertThat(entityReturned).isNotPresent();

	}

	@Test
	@Order(5)
	void findByEntityId() {

		Sensor entityRef = service.save(createTestEntity());

		Optional<Sensor> entityReturned = service.findById(entityRef);

		Assertions.assertThat(entityReturned)
		.isPresent()
		.contains(entityRef);

	}

	@Test
	@Order(6)
	void findByEntityIdFails() {

		Sensor entityRef = createTestEntity(); 
		
		entityRef.setId(UUID.randomUUID());	

		Optional<Sensor> entityReturned = service.findById(entityRef);

		Assertions.assertThat(entityReturned).isNotPresent();

	}

	@Test
	@Order(7)
	@Rollback(value = false)
	void update() {

		Sensor entityRef = service.save(createTestEntity());

		entityRef.setName("NewTestCreateSensor_" + Double.valueOf(Math.random()).intValue());

		Sensor entityUpdated = service.save(entityRef);

		Assertions.assertThat(entityUpdated.getName()).startsWith("NewTestCreateSensor_");

	}

	@Test
	@Order(8)
	@Rollback(value = false)
	void delete() {

		Sensor entityRef = service.save(createTestEntity());

		UUID entifyRefId = entityRef.getId();

		Assertions.assertThat(entifyRefId).isNotNull();

		service.delete(entityRef);

		Optional<Sensor> entityDeleted = service.findById(entifyRefId);

		Assertions.assertThat(entityDeleted).isNotPresent();
	}

	@Test
	@Order(9)
	@Rollback(value = false)
	void deleteById() {

		Sensor entityRef = service.save(createTestEntity());

		UUID entifyRefId = entityRef.getId();

		Assertions.assertThat(entifyRefId).isNotNull();

		service.delete(entifyRefId);

		Optional<Sensor> entityDeleted = service.findById(entifyRefId);

		Assertions.assertThat(entityDeleted).isNotPresent();
	}

}

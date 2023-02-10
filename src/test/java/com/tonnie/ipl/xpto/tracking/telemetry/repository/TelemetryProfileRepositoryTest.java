package com.tonnie.ipl.xpto.tracking.telemetry.repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.tonnie.ipl.xpto.tracking.telemetry.model.Sensor;
import com.tonnie.ipl.xpto.tracking.telemetry.model.TelemetryProfile;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TelemetryProfileRepositoryTest {

	@Autowired
	private SensorRepository sensorRepository;

	@Autowired
	private TelemetryProfileRepository repository;

	private TelemetryProfile createTestEntity() {

		Set<Sensor> sensors = new HashSet<>(sensorRepository.findAll());

		return TelemetryProfile.builder().name("TestCreateTlmProfile_" + Math.random()).sensors(sensors).build();
	}

	@Test
	@Order(1)
	void list() {

		Assertions.assertThat(repository.findAll()).hasSize(3);

	}

	@Test
	@Order(2)
	@Rollback(value = false)
	void save() {

		TelemetryProfile entity = repository.save(createTestEntity());

		Assertions.assertThat(entity.getId()).isNotNull();

	}

	@Test
	@Order(3)
	void findById() {

		TelemetryProfile entityRef = repository.save(createTestEntity());

		Optional<TelemetryProfile> entityReturned = repository.findById(entityRef.getId());

		Assertions.assertThat(entityReturned).isPresent().contains(entityRef);

	}

	@Test
	@Order(4)
	void findByIdFails() {

		Optional<TelemetryProfile> entityReturned = repository.findById(UUID.randomUUID());

		Assertions.assertThat(entityReturned).isNotPresent();

	}

	@Test
	@Order(5)
	@Rollback(value = false)
	void updateSensor() {

		TelemetryProfile entityRef = repository.save(createTestEntity());

		entityRef.setName("NewTestCreateTlmProfile_" + Double.valueOf(Math.random()).intValue());
		TelemetryProfile entityUpdated = repository.save(entityRef);

		Assertions.assertThat(entityUpdated.getName()).startsWith("NewTestCreateTlmProfile_");

	}

	@Test
	@Order(6)
	@Rollback(value = false)
	void deleteSensor() {

		TelemetryProfile entityRef = repository.save(createTestEntity());

		UUID entifyRefId = entityRef.getId();

		Assertions.assertThat(entifyRefId).isNotNull();

		repository.delete(entityRef);

		Optional<TelemetryProfile> entityDeleted = repository.findById(entifyRefId);

		Assertions.assertThat(entityDeleted).isNotPresent();
	}

}

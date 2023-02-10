package com.tonnie.ipl.xpto.tracking.telemetry.repository;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.tonnie.ipl.xpto.tracking.telemetry.enums.SensorType;
import com.tonnie.ipl.xpto.tracking.telemetry.model.Sensor;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SensorRepositoryTest {

	@Autowired
    private SensorRepository repository;
	
	private Sensor createTestEntity() {
		return Sensor.builder()
					.maxValue(390.0d)
					.minValue(0.0d)
					.name("TestSensor_" + Math.random())
					.type(SensorType.SPEED)
					.unit("Km/h").build();
	}

	@Test
	@Order(1)
	void list(){
		
		Assertions.assertThat(repository.findAll()).hasSize(3);
		
	}

	@Test
    @Order(2)
    @Rollback(value = false)
    void save(){

    	Sensor entity = repository.save(createTestEntity());

        Assertions.assertThat(entity.getId()).isNotNull();
        
    }
       

    @Test
    @Order(3)
    void findById(){

    	Sensor entityRef = repository.save(createTestEntity());
    	
    	Optional<Sensor> entityReturned = repository.findById(entityRef.getId());
    	
    	Assertions.assertThat(entityReturned)
    	.isPresent()
    	.contains(entityRef);

    }
    
    @Test
    @Order(4)
    void findByIdFails(){

    	Optional<Sensor> entityReturned = repository.findById(UUID.randomUUID());
    	
    	Assertions.assertThat(entityReturned).isNotPresent();

    }

    @Test
    @Order(5)
    @Rollback(value = false)
    void updateSensor(){

    	Sensor entityRef = repository.save(createTestEntity());
    	
    	entityRef.setName("NewTestSensor_"+Double.valueOf(Math.random()).intValue());

    	Sensor entityUpdated =  repository.save(entityRef);

        Assertions.assertThat(entityUpdated.getName()).startsWith("NewTestSensor_");

    }

    @Test
    @Order(6)
    @Rollback(value = false)
    void deleteSensor(){

    	Sensor entityRef = repository.save(createTestEntity());
    	
    	UUID entifyRefId = entityRef.getId();
    	
        Assertions.assertThat(entifyRefId).isNotNull();

       repository.delete(entityRef);
       
       Optional<Sensor> entityDeleted = repository.findById(entifyRefId);

        Assertions.assertThat(entityDeleted).isNotPresent();
    }

	
}

package com.tonnie.ipl.xpto.tracking.telemetry.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.tonnie.ipl.xpto.tracking.telemetry.model.Sensor;

@Repository
public interface SensorRepository extends IBaseRepository<Sensor, UUID>{
  

}

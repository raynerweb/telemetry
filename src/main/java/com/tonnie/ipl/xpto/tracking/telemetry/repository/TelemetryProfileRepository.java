package com.tonnie.ipl.xpto.tracking.telemetry.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.tonnie.ipl.xpto.tracking.telemetry.model.TelemetryProfile;

@Repository
public interface TelemetryProfileRepository extends IBaseRepository<TelemetryProfile, UUID>{
  

}

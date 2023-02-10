package com.tonnie.ipl.xpto.tracking.telemetry.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tonnie.ipl.xpto.tracking.telemetry.model.TelemetryProfile;
import com.tonnie.ipl.xpto.tracking.telemetry.repository.TelemetryProfileRepository;
import com.tonnie.ipl.xpto.tracking.telemetry.service.ITelemetryProfileService;

@Service
public class TelemetryProfileService extends BaseEntityService<UUID, TelemetryProfile, TelemetryProfileRepository>
    implements ITelemetryProfileService {

  public TelemetryProfileService(TelemetryProfileRepository repository) {
		super(repository);
  }

}

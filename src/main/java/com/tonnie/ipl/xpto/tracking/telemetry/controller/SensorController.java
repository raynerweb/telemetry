package com.tonnie.ipl.xpto.tracking.telemetry.controller;

import static com.tonnie.ipl.xpto.tracking.telemetry.util.Constants.TRACE_ID;
import static com.tonnie.ipl.xpto.tracking.telemetry.util.Constants.X_TRACE_ID;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tonnie.ipl.xpto.tracking.telemetry.exception.EntityNotFoundException;
import com.tonnie.ipl.xpto.tracking.telemetry.mapper.MapperDtoEntity;
import com.tonnie.ipl.xpto.tracking.telemetry.model.Sensor;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.api.SensorsApi;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.CreateSensorRequestDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.CreateSensorResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.GetSensorResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.ListSensorsResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.UpdateSensorRequestDto;
import com.tonnie.ipl.xpto.tracking.telemetry.service.ISensorService;
import com.tonnie.ipl.xpto.tracking.telemetry.util.Messages;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SensorController implements SensorsApi {

	private final ISensorService service;
	private final MapperDtoEntity mapper;

	@Override
	public ResponseEntity<CreateSensorResponseDto> createSensor(CreateSensorRequestDto createSensorRequestDto) {

		HttpHeaders headers = new HttpHeaders();

		headers.set(X_TRACE_ID, MDC.get(TRACE_ID));

		Sensor newEntity = mapper.mapDtoToEntity(createSensorRequestDto);

		newEntity = service.save(newEntity);

		return new ResponseEntity<>(mapper.mapEntityToCreateResponseDto(newEntity), headers, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Void> deleteSensor(String sensorId) {

		HttpHeaders headers = new HttpHeaders();

		headers.set(X_TRACE_ID, MDC.get(TRACE_ID));

		Sensor persistedEntity = service.findById(UUID.fromString(sensorId)).orElseThrow(
				() -> new EntityNotFoundException(String.format(Messages.SENSOR_NOT_FOUND_FOR_ID, sensorId)));

		service.delete(persistedEntity);

		return ResponseEntity.noContent().headers(headers).build();
	}

	@Override
	public ResponseEntity<GetSensorResponseDto> getSensor(String sensorId) {

		HttpHeaders headers = new HttpHeaders();

		headers.set(X_TRACE_ID, MDC.get(TRACE_ID));

		Sensor persistedEntity = service.findById(UUID.fromString(sensorId)).orElseThrow(
				() -> new EntityNotFoundException(String.format(Messages.SENSOR_NOT_FOUND_FOR_ID, sensorId)));

		return ResponseEntity.ok().headers(headers).body(mapper.mapEntityToDto(persistedEntity));
	}

	@Override
	public ResponseEntity<ListSensorsResponseDto> listSensors() {

		HttpHeaders headers = new HttpHeaders();

		headers.set(X_TRACE_ID, MDC.get(TRACE_ID));

		ListSensorsResponseDto responseDto = mapper.convertSensorCollectionToListDTO(service.findAll());

		return ResponseEntity.ok().headers(headers).body(responseDto);

	}

	@Override
	public ResponseEntity<Void> updateSensor(String sensorId, UpdateSensorRequestDto updateSensorRequestDto) {

		HttpHeaders headers = new HttpHeaders();

		headers.set(X_TRACE_ID, MDC.get(TRACE_ID));

		Sensor persistedEntity = service.findById(UUID.fromString(sensorId)).orElseThrow(
				() -> new EntityNotFoundException(String.format(Messages.SENSOR_NOT_FOUND_FOR_ID, sensorId)));

		Sensor newEntity = mapper.mapDtoToEntity(updateSensorRequestDto);

		persistedEntity.setMaxValue(newEntity.getMaxValue());
		persistedEntity.setMinValue(newEntity.getMinValue());
		persistedEntity.setName(newEntity.getName());
		persistedEntity.setType(newEntity.getType());
		persistedEntity.setUnit(newEntity.getUnit());

		service.update(persistedEntity);

		return ResponseEntity.noContent().headers(headers).build();
	}
}

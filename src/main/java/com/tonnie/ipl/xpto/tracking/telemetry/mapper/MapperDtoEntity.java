package com.tonnie.ipl.xpto.tracking.telemetry.mapper;

import static java.util.Objects.nonNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.tonnie.ipl.xpto.tracking.telemetry.enums.SensorType;
import com.tonnie.ipl.xpto.tracking.telemetry.model.Sensor;
import com.tonnie.ipl.xpto.tracking.telemetry.model.TelemetryProfile;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.CreateSensorRequestDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.CreateSensorResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.CreateTelemetryProfileRequestDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.CreateTelemetryProfileResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.EnumSensorTypeDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.GetSensorResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.GetTelemetryProfileResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.ListSensorsResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.ListTelemetryProfilesResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.UpdateSensorRequestDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.UpdateTelemetryProfileRequestDto;
import com.tonnie.ipl.xpto.tracking.telemetry.service.impl.SensorService;

@Mapper(componentModel = "spring")
public interface MapperDtoEntity {

	// Named Converters
	@Named("ConvertEnumSensorTypeDtoToSensorType")
	default SensorType convertEnumSensorTypeDtoToSensorType(EnumSensorTypeDto type) {
		return SensorType.valueOf(type.getValue());
	}
	@Named("ConvertSensorTypeToSensorEnumSensorTypeDto")
	default EnumSensorTypeDto convertSensorTypeToSensorEnumSensorTypeDto(SensorType type) {
		return EnumSensorTypeDto.fromValue(type.name());
	}
	
	@Named("ConvertUUIDToString")
	default String convertUUIDToString(UUID id) {
		return id.toString();
	}
	
	@Named("convertDTOArrayToCollection")
	default Set<Sensor> convertDTOArrayToCollection(List<String> ids) {
		return nonNull(ids)?SensorService.convertSetIdsToListSensors(ids):new HashSet<>();
	}
	
	default ListSensorsResponseDto convertSensorCollectionToListDTO(Collection<Sensor> entities) {
		ListSensorsResponseDto responseDto = new ListSensorsResponseDto();
		entities.forEach(sensor -> responseDto.addContentItem(mapEntityToDto(sensor)));
		responseDto.setTotalResults((long)entities.size());
		return responseDto;
	}
	
	default ListTelemetryProfilesResponseDto convertTelemetryProfileCollectionToListDTO(Collection<TelemetryProfile> entities) {
		ListTelemetryProfilesResponseDto responseDto = new ListTelemetryProfilesResponseDto();
		entities.forEach(sensor -> responseDto.addContentItem(mapEntityToDto(sensor)));
		responseDto.setTotalResults( (long) entities.size());
		return responseDto;
	}
	
	
	// DTO to Entity
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "sensorType", target = "type", qualifiedByName = "ConvertEnumSensorTypeDtoToSensorType")
	Sensor mapDtoToEntity(CreateSensorRequestDto dto);
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "sensorType", target = "type", qualifiedByName = "ConvertEnumSensorTypeDtoToSensorType")
	Sensor mapDtoToEntity(UpdateSensorRequestDto dto);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "sensors", target = "sensors", qualifiedByName = "convertDTOArrayToCollection")
	TelemetryProfile mapDtoToEntity(CreateTelemetryProfileRequestDto dto);
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "sensors", target = "sensors", qualifiedByName = "convertDTOArrayToCollection")
	TelemetryProfile mapDtoToEntity(UpdateTelemetryProfileRequestDto dto);
	

	// Entity to DTO
	@Mapping(source = "type", target = "sensorType", qualifiedByName = "ConvertSensorTypeToSensorEnumSensorTypeDto")
	@Mapping(source = "id", target = "sensorId", qualifiedByName = "ConvertUUIDToString")
	GetSensorResponseDto mapEntityToDto(Sensor entity); 
	@Mapping(source = "id", target = "sensorId", qualifiedByName = "ConvertUUIDToString")
	CreateSensorResponseDto mapEntityToCreateResponseDto(Sensor entity); 

	@Mapping(source = "id", target = "telemetryprofileId", qualifiedByName = "ConvertUUIDToString")
	GetTelemetryProfileResponseDto mapEntityToDto(TelemetryProfile entity); 
	@Mapping(source = "id", target = "telemetryprofileId", qualifiedByName = "ConvertUUIDToString")
	CreateTelemetryProfileResponseDto mapEntityToCreateResponseDto(TelemetryProfile entity); 
	

	

}
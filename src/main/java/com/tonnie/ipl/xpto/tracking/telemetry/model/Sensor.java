package com.tonnie.ipl.xpto.tracking.telemetry.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.tonnie.ipl.xpto.tracking.telemetry.enums.SensorType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sensors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor implements IEntity<UUID> {

	@Id
	@Column(name = "sensors_id")
	@GeneratedValue
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private SensorType type;
	
	@Column(name = "min_value")
	private Double minValue;
	
	@Column(name = "max_value")
	private Double maxValue;
	
	@Column(name = "unit")
	private String unit;

}

package com.tonnie.ipl.xpto.tracking.telemetry.exception;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.ErrorResponseDto;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.ErrorTypeEnumDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpError implements Serializable {

	private static final long serialVersionUID = -1183795184299420291L;
	@JsonIgnore
	private HttpStatus status;
	private transient ErrorResponseDto errorResponseDto;

	public static HttpError create(ErrorsEnum errorEnum, HttpStatus status, String details) {

		ErrorResponseDto responseDto = new ErrorResponseDto();
		responseDto.setCode(errorEnum.code);
		responseDto.setType(ErrorTypeEnumDto.fromValue(errorEnum.type.name()));
		responseDto.setMessage(errorEnum.message);
		responseDto.setDetails(details);

		return new HttpError(status, responseDto);

	}
}
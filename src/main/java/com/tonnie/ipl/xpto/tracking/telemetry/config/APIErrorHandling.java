package com.tonnie.ipl.xpto.tracking.telemetry.config;

import static com.tonnie.ipl.xpto.tracking.telemetry.util.Constants.TRACE_ID;
import static com.tonnie.ipl.xpto.tracking.telemetry.util.Constants.X_TRACE_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.postgresql.util.PSQLException;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.tonnie.ipl.xpto.tracking.telemetry.exception.EntityAlreadyExistsException;
import com.tonnie.ipl.xpto.tracking.telemetry.exception.EntityNotFoundException;
import com.tonnie.ipl.xpto.tracking.telemetry.exception.ErrorsEnum;
import com.tonnie.ipl.xpto.tracking.telemetry.exception.HttpException;
import com.tonnie.ipl.xpto.tracking.telemetry.openapi.model.ErrorResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class APIErrorHandling {

  @ExceptionHandler(HttpException.class)
  public ResponseEntity<ErrorResponseDto> handleHttpException(HttpException e) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(X_TRACE_ID, MDC.get(TRACE_ID));
    headers.setContentType(MediaType.APPLICATION_JSON);
    log.debug(e.getMessage(), e);
    return new ResponseEntity<>(e.getHttpError().getErrorResponseDto(), headers,
        e.getHttpError().getStatus());
  }

  @ExceptionHandler({EntityAlreadyExistsException.class})
  public ResponseEntity<ErrorResponseDto> handleEntityAlreadyExistsException(
      EntityAlreadyExistsException e) {
	  log.debug(e.getMessage(), e);
    return handleHttpException(
        new HttpException(ErrorsEnum.CONFLICT, HttpStatus.CONFLICT,
            e.getMessage()));
  }

  @ExceptionHandler({EntityNotFoundException.class})
  public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException e) {
	  log.debug(e.getMessage(), e);
    return handleHttpException(
        new HttpException(ErrorsEnum.NOT_FOUND, HttpStatus.NOT_FOUND,
            e.getMessage()));
  }
 
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    String details =
        e.getBindingResult().getFieldErrors().stream()
            .map(f -> String.join(" ", Arrays.asList(f.getField(), f.getDefaultMessage())))
            .sorted()
            .collect(Collectors.joining(". "));
    log.debug(details, e);
    return handleHttpException(
            new HttpException(ErrorsEnum.INVALID_REQUEST_PAYLOAD, HttpStatus.BAD_REQUEST,
                details));
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {

    String errorMessage;
    int idx = Objects.requireNonNull(e.getMessage()).lastIndexOf('.');

    if (idx < 0) {
      errorMessage = "The request body is malformed. Please validate it.";
    } else {
      errorMessage = "'" + e.getMessage().substring(idx + 1) + "' is not a valid input.";
    }
    log.debug(errorMessage, e);
    return handleHttpException(
            new HttpException(ErrorsEnum.INVALID_REQUEST_PAYLOAD, HttpStatus.BAD_REQUEST,
                errorMessage));
  }

  @ExceptionHandler({PSQLException.class})
  public ResponseEntity<ErrorResponseDto> handlePSQLException(PSQLException e) {
	log.debug(e.getMessage(), e);
    return handleHttpException(
        new HttpException(ErrorsEnum.INVALID_REQUEST_PAYLOAD_PERSISTENCE, HttpStatus.BAD_REQUEST,
            e.getMessage()));
  }

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(
      ConstraintViolationException e) {
    int firstDotPosition = e.getMessage().indexOf(".");
    log.debug(e.getMessage(), e);
    return handleHttpException(
        new HttpException(ErrorsEnum.INVALID_REQUEST_PAYLOAD_PERSISTENCE, HttpStatus.BAD_REQUEST,
            e.getMessage().substring(firstDotPosition + 1)));
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
	log.debug(e.getMessage(), e);
    return handleHttpException(
        new HttpException(ErrorsEnum.GENERIC,
            HttpStatus.INTERNAL_SERVER_ERROR, String.format("Internal error: %s",
            e.getClass().getSimpleName(), e.getMessage())));
  }

}
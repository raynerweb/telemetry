package com.tonnie.ipl.xpto.tracking.telemetry.exception;

public class EntityAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -743643339875321639L;

	public EntityAlreadyExistsException(String message) {
		super(message);
	}
}

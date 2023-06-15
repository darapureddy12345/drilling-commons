package com.bh.drillingcommons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -616140611896027284L;

	/**
	 * Default Constructor
	 */
	public BadRequestException() {
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public BadRequestException(final String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 *            -Throwable
	 */
	public BadRequestException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            -String
	 * @param cause
	 *            -Throwable
	 */
	public BadRequestException(final String message, final Throwable cause) {
		super(message, cause);
	}

}

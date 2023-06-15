package com.bh.drillingcommons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor
	 */
	public ForbiddenException() {
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public ForbiddenException(final String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 *            -Throwable
	 */
	public ForbiddenException(final Throwable cause) {
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
	public ForbiddenException(final String message, final Throwable cause) {
		super(message, cause);
	}

}

package com.bh.drillingcommons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor
	 */
	public UnauthorizedException() {
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public UnauthorizedException(final String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 *            -Throwable
	 */
	public UnauthorizedException(final Throwable cause) {
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
	public UnauthorizedException(final String message, final Throwable cause) {
		super(message, cause);
	}

}

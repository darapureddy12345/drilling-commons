package com.bh.drillingcommons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 454845964658459356L;
	
	/**
	 * Default Constructor
	 */
	public SystemException() {
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public SystemException(final String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 *            -Throwable
	 */
	public SystemException(final Throwable cause) {
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
	public SystemException(final String message, final Throwable cause) {
		super(message, cause);
	}

}

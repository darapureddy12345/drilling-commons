package com.bh.drillingcommons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 3660514465667022736L;
	
	/**
	 * Default Constructor
	 */
	public BusinessException() {
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public BusinessException(final String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 *            -Throwable
	 */
	public BusinessException(final Throwable cause) {
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
	public BusinessException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
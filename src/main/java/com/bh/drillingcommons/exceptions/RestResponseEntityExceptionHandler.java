package com.bh.drillingcommons.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { ClientErrorException.class, ServerErrorException.class })
	protected ResponseEntity<Object> handleConflict(WebApplicationException ex, WebRequest request) {
		int statusCode = ex.getResponse().getStatus();
		String error = ex.getResponse().getStatusInfo().getReasonPhrase();

		ExceptionResponseEntity response = new ExceptionResponseEntity();
		response.setError(error);
		response.setMessage(ex.getLocalizedMessage());
		response.setPath(request.getDescription(false).substring(4));
		response.setTimestamp(LocalDateTime.now());
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.valueOf(statusCode), request);
	}
	
	@ExceptionHandler(value = BadRequestException.class)
	protected ResponseEntity<Object> handleBadRequestException(BadRequestException bre) {
		return new ResponseEntity<>(bre.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = SystemException.class)
	protected ResponseEntity<Object> handleSystemException(SystemException se) {
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = BusinessException.class)
	protected ResponseEntity<Object> handleBusinessException(BusinessException se) {
		return new ResponseEntity<>(se.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}
	
	@JsonInclude(Include.NON_NULL)
	public static class ExceptionResponseEntity {
		private String error;
		private String message;
		private String path;
		private String timestamp;

		/**
		 * @return the error
		 */
		public String getError() {
			return error;
		}

		/**
		 * @param error
		 *            the error to set
		 */
		public void setError(String error) {
			this.error = error;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message
		 *            the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @return the path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @param path
		 *            the path to set
		 */
		public void setPath(String path) {
			this.path = path;
		}

		/**
		 * @return the timestamp
		 */
		public String getTimestamp() {
			return timestamp;
		}

		/**
		 * @param timestamp
		 *            the timestamp to set
		 */
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		/**
		 * @param timestamp
		 *            the timestamp to set
		 */
		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp.format(DateTimeFormatter.ISO_DATE_TIME);
		}
	}
}

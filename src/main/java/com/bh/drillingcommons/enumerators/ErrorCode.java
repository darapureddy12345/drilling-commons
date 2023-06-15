package com.bh.drillingcommons.enumerators;

import javax.ws.rs.core.Response;

public enum ErrorCode {
	
    INTERNAL ("-111111", Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    BAD_REQUEST ("000000", Response.Status.BAD_REQUEST, "Bad Request"),
    REQUIRED ("111111", Response.Status.BAD_REQUEST , "Request Missing Required Field"), 
    INVALID ("222222", Response.Status.BAD_REQUEST , "Request Contains Invalid Field"), 
    CONVERSION ("333333", Response.Status.BAD_REQUEST, "Request Requires Unrecognized Conversion Type"),
    CONFLICT ("444444", Response.Status.CONFLICT, "Request Conflicts With Existing Elements");

	private String errorId;
	private Response.Status httpStatus;
	private String defaultMessage;

	ErrorCode(String errorId, Response.Status httpStatus, String defaultMessage) {
		this.errorId = errorId;
		this.httpStatus = httpStatus;
		this.defaultMessage = defaultMessage;
	}

	public String getErrorId() {
		return this.errorId;
	}

	public Response.Status getResponseStatus() {
		return this.httpStatus;
	}

	public String getDefaultMessage() {
		return this.defaultMessage;
	}
}

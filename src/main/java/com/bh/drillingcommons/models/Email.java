package com.bh.drillingcommons.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Email implements Serializable {

	private static final long serialVersionUID = -6091552657687964806L;

	private String from;
	
	private String to;
	
	private String[] cc;
	
	private String[] bcc;
	
	private String subject;
	
	private String body;
	
	public Email() {
	}
	
	public Email(String from, String to, String[] cc, String[] bcc, String subject, String body) {
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.body = body;
	}
}

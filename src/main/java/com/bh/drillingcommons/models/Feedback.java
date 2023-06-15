package com.bh.drillingcommons.models;

import java.io.Serializable;

import lombok.Data;


@Data 
public class Feedback implements Serializable {
	
	private static final long serialVersionUID = -1760031714397900843L;
	private String feedbackName;
	private String validationMsg;
	private String subject;
	private String body;
	private String videoName;
	private String userName;
	private String emailId;
	private String reportcomment;
	private String pageName;
	private String customerName;
	private String firstName;
	private String lastName;
	private String userId;
	private String companyName;
	
}
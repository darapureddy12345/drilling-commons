package com.bh.drillingcommons.models;

import java.io.Serializable;

import lombok.Data;

/**
 * {@link NotificationRelease} object
 * 
 * @author BH
 *
 */
@Data
public class NotificationRelease implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String subject;
	
	private String mailContent;
	
	private String web;
	
	private String geUser;
	
	private String email;
	
	private String additionalAudience;
	
	private String[] customersList;
	
	private String customer;
	
	private String webId;
}

